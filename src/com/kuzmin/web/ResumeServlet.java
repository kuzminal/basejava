package com.kuzmin.web;

import com.kuzmin.Config;
import com.kuzmin.model.*;
import com.kuzmin.storage.Storage;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.YearMonth;
import java.util.*;

public class ResumeServlet extends HttpServlet {
    private Storage storage;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        storage = Config.get().getStorage();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.setCharacterEncoding("UTF-8");
        String uuid = request.getParameter("uuid") != null ? request.getParameter("uuid") : "";
        String fullName = request.getParameter("fullName") != null ? request.getParameter("fullName") : "";
        String postAction = request.getParameter("postAction") != null ? request.getParameter("postAction") : "";
        Resume resume;
        if (uuid != null) {
            resume = storage.get(uuid);
            switch (postAction) {
                case "saveContact":
                    saveContact(request, resume);
                    saveAndSendRedirectToEdit(resume, response);
                    return;
                case "addSection":
                    addSection(request, resume);
                    saveAndSendRedirectToEdit(resume, response);
                    return;
                case "saveSection":
                    saveSection(request, resume);
                    saveAndSendRedirectToEdit(resume, response);
                    return;
                case "savePosition":
                    savePosition(request, resume);
                    saveAndSendRedirectToEdit(resume, response);
                    return;
                case "saveResume":
                    resume.setFullName(fullName);
                    fillContacts(request, resume);
                    fillSections(request, resume);
                    storage.update(resume);
                    break;
                case "":
                    response.sendRedirect("resume?uuid=" + resume.getUuid() + "&action=edit");
                default:
                    throw new IllegalArgumentException("Action " + postAction + " is illegal");
            }
        } else {
            resume = new Resume(fullName);
            fillContacts(request, resume);
            fillSections(request, resume);
            storage.save(resume);
        }
        response.sendRedirect("resume");
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String uuid = request.getParameter("uuid");
        String action = request.getParameter("action");
        SectionType sectionType = request.getParameter("sectionType") != null ? SectionType.valueOf(request.getParameter("sectionType")) : null;
        String url;
        if (action == null) {
            request.setAttribute("resumes", storage.getAllSorted());
            request.getRequestDispatcher("/WEB-INF/jsp/list.jsp").forward(request, response);
        }
        Resume resume;
        Organization org = null;
        switch (action) {
            case "delete":
                storage.delete(uuid);
                response.sendRedirect("resume");
                return;
            case "deleteContact":
                resume = storage.get(uuid);
                resume.removeContact(ContactType.valueOf(request.getParameter("contact")));
                saveAndSendRedirectToEdit(resume, response);
                return;
            case "deleteSection":
                resume = storage.get(uuid);
                resume.removeSection(sectionType);
                saveAndSendRedirectToEdit(resume, response);
                return;
            case "deleteOrganisation":
                resume = storage.get(uuid);
                org = getOrganisation(sectionType, request, resume);
                OrganizationSection organizationSection = (OrganizationSection) resume.getSection(sectionType);
                organizationSection.removeOrganisation(org);
                saveAndSendRedirectToEdit(resume, response);
                return;
            case "deleteExperience":
                resume = storage.get(uuid);
                Experience exp;
                org = getOrganisation(sectionType, request, resume);
                exp = org.getExperiences().stream()
                        .filter(e -> e.getStartDate().equals(YearMonth.parse(request.getParameter("expstart")))
                                && e.getEndDate().equals(YearMonth.parse(request.getParameter("expend")))
                                && e.getPosition().equals(request.getParameter("position")))
                        .findFirst().orElse(new Experience());
                org.getExperiences().remove(exp);
                saveAndSendRedirectToEdit(resume, response);
                return;
            case "view":
                url = "/WEB-INF/jsp/view.jsp";
                resume = storage.get(uuid);
                break;
            case "edit":
                resume = storage.get(uuid);
                url = "/WEB-INF/jsp/edit.jsp";
                break;
            case "add":
                resume = new Resume();
                url = "/WEB-INF/jsp/edit.jsp";
                break;
            case "addSection":
                resume = storage.get(uuid);
                url = "/WEB-INF/jsp/addSection.jsp";
                break;
            case "addPosition":
                resume = storage.get(uuid);
                org = getOrganisation(sectionType, request, resume);
                url = "/WEB-INF/jsp/addPosition.jsp";
                break;
            case "newSection":
                resume = storage.get(uuid);
                url = "/WEB-INF/jsp/newSection.jsp";
                break;
            case "newContact":
                resume = storage.get(uuid);
                url = "/WEB-INF/jsp/newContact.jsp";
                break;
            default:
                throw new IllegalArgumentException("Action " + action + " is illegal");
        }
        request.setAttribute("resume", resume);
        request.setAttribute("sectionType", sectionType);
        request.setAttribute("organisation", org);
        request.getRequestDispatcher((url)
        ).forward(request, response);
    }

    private Organization getOrganisation(SectionType sectionType, HttpServletRequest request, Resume resume) {
        OrganizationSection orgSec = (OrganizationSection) resume.getSection(sectionType);
        return orgSec.getOrganizations().stream().filter(o -> o.getTitle().equals(request.getParameter("organisation"))).findFirst().orElse(new Organization());
    }

    private void fillContacts(HttpServletRequest request, Resume resume) {
        for (ContactType type : ContactType.values()) {
            String value = request.getParameter(type.name()) != null ? request.getParameter(type.name()) : "";
            if (value.trim().length() != 0) {
                resume.addContact(type, value);
            } else {
                resume.getContacts().remove(type);
            }
        }
    }

    private void fillSections(HttpServletRequest request, Resume resume) {
        for (SectionType type : SectionType.values()) {
            String value = request.getParameter(type.name()) != null ? request.getParameter(type.name()) : "";
            if (value.trim().length() != 0) {
                switch (type) {
                    case PERSONAL:
                    case OBJECTIVE: {
                        TextSection textSection = new TextSection(type, value);
                        resume.addSection(type, textSection);
                        break;
                    }
                    case ACHIEVEMENT:
                    case QUALIFICATIONS: {
                        List<String> text = Arrays.asList(value.split("/r/n"));
                        TextListSection textSection = new TextListSection(type, text);
                        resume.addSection(type, textSection);
                        break;
                    }
                    case EXPERIENCE:
                    case EDUCATION: {
                        String[] parameters = request.getParameterValues(type.name());
                        List<Organization> orgs = new ArrayList<>();
                        String[] urls = request.getParameterValues(type.name() + "url") != null ? request.getParameterValues(type.name() + "url") : new String[]{};
                        String[] descriptions = request.getParameterValues(type.name() + "description") != null ? request.getParameterValues(type.name() + "description") : new String[]{};
                        for (int i = 0; i < parameters.length; i++) {
                            String name = descriptions.length == parameters.length ? descriptions[i] : "";
                            if (name.length() > 0) {
                                String[] startDates = request.getParameterValues(type.name() + i + "startDate") != null ? request.getParameterValues(type.name() + i + "startDate") : new String[]{};
                                String[] endDates = request.getParameterValues(type.name() + i + "endDate") != null ? request.getParameterValues(type.name() + i + "endDate") : new String[]{};
                                String[] position = request.getParameterValues(type.name() + i + "position") != null ? request.getParameterValues(type.name() + i + "position") : new String[]{};
                                String[] descriptionsExp = request.getParameterValues(type.name() + i + "dscr") != null ? request.getParameterValues(type.name() + i + "dscr") : new String[]{};
                                List<Experience> positions = new ArrayList<>();
                                if (startDates.length > 0 && endDates.length > 0) {
                                    for (int j = 0; j < position.length; j++) {
                                        positions.add(new Experience(YearMonth.parse(startDates[j]), YearMonth.parse(endDates[j]), descriptionsExp[j], position[j]));
                                    }
                                    orgs.add(new Organization(name, urls[i], positions));
                                }
                            }
                        }
                        resume.addSection(type, new OrganizationSection(type, orgs));
                        break;
                    }
                }
            } else {
                resume.getSections().remove(type);
            }
        }
    }

    private void saveContact(HttpServletRequest request, Resume resume) {
        String contactType = request.getParameter("contactType");
        String contactValue = request.getParameter("contactValue");
        if ((contactType != null && contactType.trim().length() != 0) && (contactValue != null && contactValue.trim().length() != 0)) {
            resume.addContact(ContactType.valueOf(contactType), contactValue);
        }
    }

    private void addSection(HttpServletRequest request, Resume resume) {
        String sectionType = request.getParameter("sectionType");
        if (sectionType != null && sectionType.trim().length() != 0) {
            SectionType type = SectionType.valueOf(sectionType);
            switch (type) {
                case PERSONAL:
                case OBJECTIVE: {
                    resume.addSection(SectionType.valueOf(sectionType), new TextSection());
                    break;
                }
                case ACHIEVEMENT:
                case QUALIFICATIONS: {
                    resume.addSection(SectionType.valueOf(sectionType), new TextListSection());
                    break;
                }
                case EXPERIENCE:
                case EDUCATION: {
                    resume.addSection(SectionType.valueOf(sectionType), new OrganizationSection());
                    break;
                }
            }
        }
    }

    private void saveSection(HttpServletRequest request, Resume resume) {
        Map<String, String> parameters = getRequestParameters(request);
        if ((parameters.get("url") != null && parameters.get("url").trim().length() != 0) && (parameters.get("description") != null && (parameters.get("description").trim().length() != 0))) {
            Organization organization = new Organization(parameters.get("description"), parameters.get("url"), new ArrayList<>());
            organization.setExperiences(Collections.singletonList(new Experience(YearMonth.parse(parameters.get("startDate")), YearMonth.parse(parameters.get("endDate")), parameters.get("dscr"), parameters.get("position"))));
            OrganizationSection orgSec = (OrganizationSection) resume.getSection(SectionType.valueOf(parameters.get("sectionType")));
            if (orgSec.getOrganizations() != null) {
                List<Organization> orgs = orgSec.getOrganizations();
                orgs.add(organization);
                orgSec.setOrganizations(orgs);
            } else {
                orgSec.setOrganizations(Collections.singletonList(organization));
            }
        }
    }

    private void savePosition(HttpServletRequest request, Resume resume) {
        Map<String, String> parameters = getRequestParameters(request);
        if (parameters.get("org") != null && parameters.get("org").trim().length() != 0) {
            OrganizationSection orgSec = (OrganizationSection) resume.getSection(SectionType.valueOf(parameters.get("sectionType")));
            Organization organisation = orgSec.getOrganizations().stream().filter(o -> o.getTitle().equals(parameters.get("org"))).findFirst().orElse(null);
            if (organisation != null) {
                List<Experience> experiences = organisation.getExperiences();
                experiences.add(new Experience(YearMonth.parse(parameters.get("startDate")), YearMonth.parse(parameters.get("endDate")), parameters.get("dscr"), parameters.get("position")));
                organisation.setExperiences(experiences);
            }
        }
    }

    private Map<String, String> getRequestParameters(HttpServletRequest request) {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("org", request.getParameter("org"));
        parameters.put("sectionType", request.getParameter("sectionType"));
        parameters.put("startDate", request.getParameter("startDate"));
        parameters.put("endDate", request.getParameter("endDate"));
        parameters.put("position", request.getParameter("position"));
        parameters.put("dscr", request.getParameter("dscr"));
        parameters.put("description", request.getParameter("description"));
        parameters.put("url", request.getParameter("url"));
        return parameters;
    }

    private void saveAndSendRedirectToEdit(Resume resume, HttpServletResponse response) throws IOException {
        storage.update(resume);
        response.sendRedirect("resume?uuid=" + resume.getUuid() + "&action=edit");
    }
}

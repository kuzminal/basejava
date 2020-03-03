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
        String uuid = request.getParameter("uuid");
        String fullName = request.getParameter("fullName") != null ? request.getParameter("fullName") : "";
        String postAction = request.getParameter("postAction");
        Resume resume;
        if (uuid != null && postAction != null) {
            switch (postAction) {
                case "saveContact":
                    resume = storage.get(uuid);
                    saveContact(request, resume);
                    storage.update(resume);
                    response.sendRedirect("resume?uuid=" + resume.getUuid() + "&action=edit");
                    return;
                case "addSection":
                    resume = storage.get(uuid);
                    addSection(request, resume);
                    storage.update(resume);
                    response.sendRedirect("resume?uuid=" + resume.getUuid() + "&action=edit");
                    return;
                case "saveSection":
                    resume = storage.get(uuid);
                    saveSection(request, resume);
                    storage.update(resume);
                    response.sendRedirect("resume?uuid=" + resume.getUuid() + "&action=edit");
                    return;
                case "savePosition":
                    resume = storage.get(uuid);
                    savePosition(request, resume);
                    storage.update(resume);
                    response.sendRedirect("resume?uuid=" + resume.getUuid() + "&action=edit");
                    return;
                case "saveResume":
                    resume = storage.get(uuid);
                    resume.setFullName(fullName);
                    fillContacts(request, resume);
                    fillSections(request, resume);
                    storage.update(resume);
                    break;
                default:
                    throw new IllegalArgumentException("Action " + postAction + " is illegal");
            }
        } else if (uuid != null) {
            resume = storage.get(uuid);
            response.sendRedirect("resume?uuid=" + resume.getUuid() + "&action=edit");
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
        String url;
        if (action == null) {
            request.setAttribute("resumes", storage.getAllSorted());
            request.getRequestDispatcher("/WEB-INF/jsp/list.jsp").forward(request, response);
        }
        Resume resume;
        SectionType sectionType = null;
        OrganizationSection orgSect = null;
        Organization org = null;
        Experience exp = null;
        switch (action) {
            case "delete":
                storage.delete(uuid);
                response.sendRedirect("resume");
                return;
            case "deleteContact":
                resume = storage.get(uuid);
                Map<ContactType, String> contacts = resume.getContacts();
                contacts.remove(ContactType.valueOf(request.getParameter("contact")));
                resume.setContacts(contacts);
                storage.update(resume);
                response.sendRedirect("resume?uuid=" + resume.getUuid() + "&action=edit");
                return;
            case "deleteSection":
                resume = storage.get(uuid);
                Map<SectionType, AbstractSection> sections = resume.getSections();
                sections.remove(SectionType.valueOf(request.getParameter("sectionType")));
                resume.setSections(sections);
                storage.update(resume);
                response.sendRedirect("resume?uuid=" + resume.getUuid() + "&action=edit");
                return;
            case "deleteOrganisation":
                resume = storage.get(uuid);
                org = getOrganistion(SectionType.valueOf(request.getParameter("sectionType")), request, resume);
                if (org != null) {
                    orgSect.getOrganizations().remove(org);
                }
                storage.update(resume);
                response.sendRedirect("resume?uuid=" + resume.getUuid() + "&action=edit");
                return;
            case "deleteExperience":
                resume = storage.get(uuid);
                org = getOrganistion(SectionType.valueOf(request.getParameter("sectionType")), request, resume);
                if (org != null) {
                    for (Experience experience : org.getExperiences()) {
                        if (experience.getStartDate().equals(YearMonth.parse(request.getParameter("expstart")))
                                && experience.getEndDate().equals(YearMonth.parse(request.getParameter("expend")))
                                && experience.getPosition().equals(request.getParameter("position"))) {
                            exp = experience;
                        }
                    }
                    if (exp != null) {
                        org.getExperiences().remove(exp);
                    }
                }
                storage.update(resume);
                response.sendRedirect("resume?uuid=" + resume.getUuid() + "&action=edit");
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
                org = getOrganistion(SectionType.valueOf(request.getParameter("sectionType")), request, resume);
                url = "/WEB-INF/jsp/addPosition.jsp";
                break;
            case "newSection":
                resume = storage.get(uuid);
                sectionType = SectionType.valueOf(request.getParameter("sectionType"));
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

    private Organization getOrganistion(SectionType sectionType, HttpServletRequest request, Resume resume) {
        OrganizationSection orgSec = (OrganizationSection) resume.getSection(sectionType);
        return orgSec.getOrganizations().stream().filter(o -> o.getTitle().equals(request.getParameter("organisation"))).findFirst().orElse(null);
    }

    private void fillContacts(HttpServletRequest request, Resume resume) {
        for (ContactType type : ContactType.values()) {
            String value = request.getParameter(type.name());
            if (value != null && value.trim().length() != 0) {
                resume.addContact(type, value);
            } else {
                resume.getContacts().remove(type);
            }
        }
    }

    private void fillSections(HttpServletRequest request, Resume resume) {
        for (SectionType type : SectionType.values()) {
            String value = request.getParameter(type.name());
            if (value != null && value.trim().length() != 0) {
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
                        String[] urls = request.getParameterValues(type.name() + "url");
                        String[] descriptions = request.getParameterValues(type.name() + "description");
                        if (urls != null && descriptions != null) {
                            for (int i = 0; i < parameters.length; i++) {
                                String name = descriptions[i];
                                if (name.length() > 0) {
                                    String[] startDates = request.getParameterValues(type.name() + i + "startDate");
                                    String[] endDates = request.getParameterValues(type.name() + i + "endDate");
                                    String[] position = request.getParameterValues(type.name() + i + "position");
                                    String[] descriptionsExp = request.getParameterValues(type.name() + i + "dscr");
                                    List<Experience> positions = new ArrayList<>();
                                    if ((startDates != null && startDates.length > 0) && (endDates != null && endDates.length > 0)) {
                                        for (int j = 0; j < position.length; j++) {
                                            positions.add(new Experience(YearMonth.parse(startDates[j]), YearMonth.parse(endDates[j]), descriptionsExp[j], position[j]));
                                        }
                                        orgs.add(new Organization(name, urls[i], positions));
                                    }
                                }
                            }
                            resume.addSection(type, new OrganizationSection(type, orgs));
                        }
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
}

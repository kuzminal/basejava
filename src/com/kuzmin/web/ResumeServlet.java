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

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.setCharacterEncoding("UTF-8");
        String uuid = request.getParameter("uuid") != null ? request.getParameter("uuid") : "";
        String fullName = request.getParameter("fullName") != null ? request.getParameter("fullName") : "";
        String postAction = request.getParameter("postAction") != null ? request.getParameter("postAction") : "";
        Resume resume = storage.get(uuid);
        switch (postAction) {
            case "saveResume":
                resume.setFullName(fullName);
                fillContacts(request, resume);
                fillSections(request, resume);
                if (!fullName.isEmpty()) {
                    storage.update(resume);
                } else {
                    storage.delete(uuid);
                }
                response.sendRedirect("resume");
                return;
            case "saveContact":
                saveContact(request, response, resume);
                break;
            case "addSection":
                addSection(request, response, resume);
                break;
            case "saveSection":
                saveSection(request, response, resume);
                break;
            case "savePosition":
                savePosition(request, response, resume);
                break;
            default:
                throw new IllegalArgumentException("Action " + postAction + " is illegal");
        }
        saveAndSendRedirectToEdit(resume, response);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String uuid = request.getParameter("uuid");
        String action = request.getParameter("action");
        SectionType sectionType = request.getParameter("sectionType") != null ? SectionType.valueOf(request.getParameter("sectionType")) : null;
        ContactType contactType = request.getParameter("contact") != null ? ContactType.valueOf(request.getParameter("contact")) : null;
        String url;
        if (action == null) {
            request.setAttribute("resumes", storage.getAllSorted());
            request.getRequestDispatcher("/WEB-INF/jsp/list.jsp").forward(request, response);
        }
        Resume resume = "add".equals(action) ? new Resume() : storage.get(uuid);
        Organization org = null;
        switch (action) {
            case "delete":
                storage.delete(uuid);
                response.sendRedirect("resume");
                return;
            case "deleteContact":
                deleteContact(resume, contactType, response);
                return;
            case "deleteSection":
                deleteSection(resume, sectionType, response);
                return;
            case "deleteOrganisation":
                org = getOrganisation(sectionType, request, resume);
                deleteOrganisation(resume, sectionType, org, response);
                return;
            case "deleteExperience":
                Experience exp;
                org = getOrganisation(sectionType, request, resume);
                exp = org.getExperiences().stream()
                        .filter(e -> e.getStartDate().equals(YearMonth.parse(request.getParameter("expstart")))
                                && e.getEndDate().equals(YearMonth.parse(request.getParameter("expend")))
                                && e.getPosition().equals(request.getParameter("position")))
                        .findFirst().orElse(new Experience());
                deleteExperience(resume, org, exp, response);
                return;
            case "view":
                url = "/WEB-INF/jsp/view.jsp";
                break;
            case "edit":
                url = "/WEB-INF/jsp/edit.jsp";
                break;
            case "add":
                storage.save(resume);
                url = "/WEB-INF/jsp/edit.jsp";
                break;
            case "addSection":
                url = "/WEB-INF/jsp/addSection.jsp";
                break;
            case "addPosition":
                org = getOrganisation(sectionType, request, resume);
                url = "/WEB-INF/jsp/addPosition.jsp";
                break;
            case "newSection":
                url = "/WEB-INF/jsp/newSection.jsp";
                break;
            case "newContact":
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
                        TextSection textSection = new TextSection(value);
                        resume.addSection(type, textSection);
                        break;
                    }
                    case ACHIEVEMENT:
                    case QUALIFICATIONS: {
                        List<String> text = Arrays.asList(value.split("/r/n"));
                        TextListSection textSection = new TextListSection(text);
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
                        resume.addSection(type, new OrganizationSection(orgs));
                        break;
                    }
                }
            } else {
                resume.getSections().remove(type);
            }
        }
    }

    private void saveContact(HttpServletRequest request, HttpServletResponse response, Resume resume) {
        String contactType = request.getParameter("contactType");
        String contactValue = request.getParameter("contactValue");
        if ((contactType != null && contactType.trim().length() != 0) && (contactValue != null && contactValue.trim().length() != 0)) {
            resume.addContact(ContactType.valueOf(contactType), contactValue);
        }
    }

    private void addSection(HttpServletRequest request, HttpServletResponse response, Resume resume) {
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

    private void saveSection(HttpServletRequest request, HttpServletResponse response, Resume resume) throws IOException {
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
        saveAndSendRedirectToEdit(resume, response);
    }

    private void savePosition(HttpServletRequest request, HttpServletResponse response, Resume resume) {
        Map<String, String> parameters = getRequestParameters(request);
        if (parameters.get("org").trim().length() != 0) {
            OrganizationSection orgSec = (OrganizationSection) resume.getSection(SectionType.valueOf(parameters.get("sectionType")));
            Organization organisation = orgSec.getOrganizations().stream().filter(o -> o.getTitle().equals(parameters.get("org"))).findFirst().orElse(new Organization());
            if (!organisation.isEmpty()) {
                organisation.addExperience(new Experience(YearMonth.parse(parameters.get("startDate")), YearMonth.parse(parameters.get("endDate")), parameters.get("dscr"), parameters.get("position")));
            }
        }
    }

    private Map<String, String> getRequestParameters(HttpServletRequest request) {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("org", request.getParameter("org") != null ? request.getParameter("org") : "");
        parameters.put("sectionType", request.getParameter("sectionType") != null ? request.getParameter("sectionType") : "");
        parameters.put("startDate", request.getParameter("startDate") != null ? request.getParameter("startDate") : "");
        parameters.put("endDate", request.getParameter("endDate") != null ? request.getParameter("endDate") : "");
        parameters.put("position", request.getParameter("position") != null ? request.getParameter("position") : "");
        parameters.put("dscr", request.getParameter("dscr") != null ? request.getParameter("dscr") : "");
        parameters.put("description", request.getParameter("description") != null ? request.getParameter("description") : "");
        parameters.put("url", request.getParameter("url") != null ? request.getParameter("url") : "");
        return parameters;
    }

    private void saveAndSendRedirectToEdit(Resume resume, HttpServletResponse response) throws IOException {
        storage.update(resume);
        response.sendRedirect("resume?uuid=" + resume.getUuid() + "&action=edit");
    }

    private void deleteContact(Resume resume, ContactType contactType, HttpServletResponse response) throws IOException {
        resume.removeContact(contactType);
        saveAndSendRedirectToEdit(resume, response);
    }

    private void deleteSection(Resume resume, SectionType sectionType, HttpServletResponse response) throws IOException {
        resume.removeSection(sectionType);
        saveAndSendRedirectToEdit(resume, response);
    }

    private void deleteOrganisation(Resume resume, SectionType sectionType, Organization organization, HttpServletResponse response) throws IOException {
        OrganizationSection organizationSection = (OrganizationSection) resume.getSection(sectionType);
        organizationSection.removeOrganisation(organization);
        saveAndSendRedirectToEdit(resume, response);
    }

    private void deleteExperience(Resume resume, Organization organization, Experience experience, HttpServletResponse response) throws IOException {
        organization.removeExperience(experience);
        saveAndSendRedirectToEdit(resume, response);
    }
}

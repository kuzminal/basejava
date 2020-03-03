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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class ResumeServlet extends HttpServlet {
    private Storage storage;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        storage = Config.get().getStorage();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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
                Map<ContactType, String> conntacts = resume.getContacts();
                conntacts.remove(ContactType.valueOf(request.getParameter("contact")));
                resume.setContacts(conntacts);
                storage.update(resume);
                response.sendRedirect("resume?uuid=" + resume.getUuid() + "&action=edit");
                return;
            case "deleteSection":
                resume = storage.get(uuid);
                Map<SectionType, AbstractSection> sections = resume.getSections();
                sections.remove(SectionType.valueOf(request.getParameter("section")));
                resume.setSections(sections);
                storage.update(resume);
                response.sendRedirect("resume?uuid=" + resume.getUuid() + "&action=edit");
                return;
            case "deleteOrganisation":
                resume = storage.get(uuid);
                orgSect = (OrganizationSection) resume.getSection(SectionType.valueOf(request.getParameter("section")));
                for (Organization organization : orgSect.getOrganizations()) {
                    if (organization.getTitle().equals(request.getParameter("organisation"))) {
                        org = organization;
                    }
                }
                if (org != null) {
                    orgSect.getOrganizations().remove(org);
                }
                storage.update(resume);
                response.sendRedirect("resume?uuid=" + resume.getUuid() + "&action=edit");
                return;
            case "deleteExperience":
                resume = storage.get(uuid);
                orgSect = (OrganizationSection) resume.getSection(SectionType.valueOf(request.getParameter("section")));
                for (Organization organization : orgSect.getOrganizations()) {
                    if (organization.getTitle().equals(request.getParameter("organisation"))) {
                        org = organization;
                    }
                }
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
                sectionType = SectionType.valueOf(request.getParameter("section"));
                String organisation = request.getParameter("organisation");
                OrganizationSection orgsec = (OrganizationSection) resume.getSection(sectionType);
                for (Organization orgn : orgsec.getOrganizations()) {
                    if (orgn.getTitle().equals(organisation)) {
                        org = orgn;
                    }
                }
                url = "/WEB-INF/jsp/addPosition.jsp";
                break;
            case "newSection":
                resume = storage.get(uuid);
                sectionType = SectionType.valueOf(request.getParameter("section"));
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
        String url = request.getParameter("url");
        String description = request.getParameter("description");
        String sectionType = request.getParameter("sectionType");
        String startDate = request.getParameter("startDate");
        String endDate = request.getParameter("endDate");
        String position = request.getParameter("position");
        String desc = request.getParameter("dscr");
        if ((url != null && url.trim().length() != 0) && (description != null && description.trim().length() != 0)) {
            Organization organization = new Organization();
            organization.setTitle(description);
            organization.setUrl(url);
            Experience exp = new Experience();
            exp.setPosition(position);
            exp.setDescription(desc);
            exp.setStartDate(YearMonth.parse(startDate));
            exp.setEndDate(YearMonth.parse(endDate));
            List<Experience> experiences = new ArrayList<>();
            experiences.add(exp);
            organization.setExperiences(experiences);
            OrganizationSection orgSec = (OrganizationSection) resume.getSection(SectionType.valueOf(sectionType));
            List<Organization> orgs = orgSec.getOrganizations();
            orgs.add(organization);
            orgSec.setOrganizations(orgs);
        }
    }

    private void savePosition(HttpServletRequest request, Resume resume) {
        String org = request.getParameter("org");
        String sectionType = request.getParameter("sectionType");
        String startDate = request.getParameter("startDate");
        String endDate = request.getParameter("endDate");
        String position = request.getParameter("position");
        String desc = request.getParameter("dscr");
        Organization organization = null;
        if (org != null && org.trim().length() != 0) {
            OrganizationSection orgSec = (OrganizationSection) resume.getSection(SectionType.valueOf(sectionType));
            for (Organization orgatniz : orgSec.getOrganizations()) {
                if (orgatniz.getTitle().equals(org)) {
                    organization = orgatniz;
                    Experience expr = new Experience();
                    expr.setPosition(position);
                    expr.setDescription(desc);
                    expr.setStartDate(YearMonth.parse(startDate));
                    expr.setEndDate(YearMonth.parse(endDate));
                    List<Experience> experiences = organization.getExperiences();
                    experiences.add(expr);
                    orgatniz.setExperiences(experiences);
                }
            }
        }
    }
}

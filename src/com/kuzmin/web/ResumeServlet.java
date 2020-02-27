package com.kuzmin.web;

import com.kuzmin.Config;
import com.kuzmin.exception.NotExistStorageException;
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
        String fullName = request.getParameter("fullName");
        Resume resume;
        try {
            resume = storage.get(uuid);
            resume.setFullName(fullName);
            fillContacts(request, resume);
            fillSections(request, resume);
            storage.update(resume);
        } catch (NotExistStorageException e) {
            resume = new Resume(uuid, fullName);
            fillContacts(request, resume);
            fillSections(request, resume);
            storage.save(resume);
        }
        response.sendRedirect("resume");
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String uuid = request.getParameter("uuid");
        String action = request.getParameter("action");
        if (action == null) {
            request.setAttribute("resumes", storage.getAllSorted());
            request.getRequestDispatcher("/WEB-INF/jsp/list.jsp").forward(request, response);
        }
        Resume resume;
        switch (action) {
            case "delete":
                storage.delete(uuid);
                response.sendRedirect("resume");
                return;
            case "view":
            case "edit":
                resume = storage.get(uuid);
                break;
            case "add":
                resume = new Resume("");
                break;
            default:
                throw new IllegalArgumentException("Action " + action + " is illegal");
        }
        request.setAttribute("resume", resume);
        request.getRequestDispatcher(
                ("view".equals(action) ? "/WEB-INF/jsp/view.jsp" : "/WEB-INF/jsp/edit.jsp")
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
                        for (int i = 0; i < parameters.length; i++) {
                            String name = descriptions[i];
                            if (name.length() > 0) {
                                String[] startDates = request.getParameterValues(type.name() + i + "startDate");
                                String[] endDates = request.getParameterValues(type.name() + i + "endDate");
                                String[] position = request.getParameterValues(type.name() + i + "position");
                                String[] descriptionsExp = request.getParameterValues(type.name() + i + "dscr");
                                List<Experience> positions = new ArrayList<>();
                                if (position.length > 0) {
                                    for (int j = 0; j < position.length; j++) {
                                        positions.add(new Experience(YearMonth.parse(startDates[j]), YearMonth.parse(endDates[j]), descriptionsExp[j], position[j]));
                                    }
                                }
                                orgs.add(new Organization(name, urls[i], positions));
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
}

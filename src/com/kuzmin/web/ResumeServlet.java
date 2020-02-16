package com.kuzmin.web;

import com.kuzmin.Config;
import com.kuzmin.model.Resume;
import com.kuzmin.storage.Storage;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class ResumeServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        Storage storage = Config.get().getStorage();
        List<Resume> resumes = storage.getAllSorted();
        response.setContentType("text/html; charset=UTF-8");
        String text = "<!DOCTYPE html PUBLIC " +
                "\"-//W3C//DTD HTML 4.01 Transitional//EN\" " +
                "\"http://www.w3.org/TR/html4/loose.dtd\"> " +
                "<html><head>" +
                "<meta http-equiv=\"Content-Type\" " +
                "content=\"text/html; charset=UTF-8\"> " +
                "<title>Resumes</title>" +
                "</head>" +
                "<body>" +
                "<h1>Список резюме</h1>" +
                "<table border=\"1\">" +
                "<tr><td>UUID</td> <td>ФИО</td></tr>";
        for (Resume resume : resumes) {
            text = text +
                    "<tr><td>" + resume.getUuid() + "</td> <td>" + resume.getFullName() + "</td></tr>";
        }

        text = text +
                "</table>" +
                "</body></html>";
        response.getWriter().write(text);
    }
}

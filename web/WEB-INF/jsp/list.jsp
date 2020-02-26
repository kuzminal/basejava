<%@ page import="com.kuzmin.model.ContactType" %>
<%@ page import="com.kuzmin.model.Resume" %>
<%@ page import="java.util.List" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: aleksejkuzmin
  Date: 25.02.2020
  Time: 07:51
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link rel="stylesheet" href="css/style.css">
    <title>Список всех резюме</title>
</head>
<body>
<section>
    <%@ include file="fragments/header.jsp"%>
    <table border="1" cellpadding="8" cellspacing="0">
        <tr class="tab_head">
            <th>Имя</th>
            <th>Email</th>
        </tr>
        <c:forEach items="${resumes}" var="resume">
            <jsp:useBean id="resume" type="com.kuzmin.model.Resume"/>
            <tr>
                <td><a href="resume?uuid=${resume.uuid}">${resume.fullName}</a></td>
                <td>${resume.getContact(ContactType.EMAIL)}</td>
            </tr>
        </c:forEach>
    </table>
    <%@ include file="fragments/footer.jsp"%>
</section>
</body>
</html>

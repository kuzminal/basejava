<%@ page import="com.kuzmin.model.ContactType" %>
<%@ page import="com.kuzmin.model.SectionType" %>
<%@ page import="com.kuzmin.model.OrganizationSection" %>
<%@ page import="com.kuzmin.model.TextListSection" %>
<%@ page import="java.time.YearMonth" %><%--
  Created by IntelliJ IDEA.
  User: aleksejkuzmin
  Date: 26.02.2020
  Time: 10:28
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link rel="stylesheet" href="css/style.css">
    <jsp:useBean id="resume" type="com.kuzmin.model.Resume" scope="request"/>
    <title>Резюме ${resume.fullName}</title>
</head>
<body>
<%@ include file="fragments/header.jsp" %>
<section>
    <form method="post" action="resume" enctype="application/x-www-form-urlencoded">
        <input type="hidden" name="uuid" value="${resume.uuid}">
        <dl>
            <dt>Имя:</dt>
            <dd><input type="text" name="fullName" size="50" value="${resume.fullName}"></dd>
        </dl>
        <h3>Контакты</h3>
        <p>
            <c:forEach var="type" items="<%=ContactType.values()%>">
                <dl>
                    <dt>${type.title}</dt>
                    <dd><input type="text" name="${type.name()}" size="30" value="${resume.getContact(type)}"></dd>
                </dl>
            </c:forEach>
        </p>
        <h3>Секции</h3>
        <p>
            <c:forEach var="section" items="<%=SectionType.values()%>">
                    <c:choose>
                      <c:when test="${section == 'EXPERIENCE'}">
                        <dl>
                            <dt>${section.title}</dt>
                            <dd>
                                <c:forEach var="organization" items="<%=((OrganizationSection)resume.getSection(SectionType.EXPERIENCE)).getOrganizations()%>">
                                    <jsp:useBean id="organization" type="com.kuzmin.model.Organization"/>
                                    <input type="text" name="title" size="50" value="${organization.title}"></br>
                                    <input type="text" name="url" size="30" value="${organization.url}"></br>
                                    <c:forEach var="exp" items="${organization.experiences}">
                                        <jsp:useBean id="exp" type="com.kuzmin.model.Experience"/>
                                        <input type="text" name="startDate" size="10" value="${exp.startDate}"></br>
                                        <input type="text" name="endDate" size="10" value="<%=exp.getEndDate().equals(YearMonth.now()) ? "по наст. врем" : exp.getEndDate()%>"></br>
                                        <input type="text" name="position" size="30" value="${exp.position}"></br>
                                        <textarea rows="5" cols="40">${exp.description}</textarea></br>
                                    </c:forEach>
                                </c:forEach>
                            </dd>
                        </dl>
                    </c:when>
                    <c:when test="${section == 'EDUCATION'}">
                        <dl>
                            <dt>${section.title}</dt>
                            <dd>
                                <c:forEach var="education" items="<%=((OrganizationSection)resume.getSection(SectionType.EDUCATION)).getOrganizations()%>">
                                    <jsp:useBean id="education" type="com.kuzmin.model.Organization"/>
                                    <input type="text" name="title" size="50" value="${education.title}"></br>
                                    <input type="text" name="url" size="30" value="${education.url}"></br>
                                    <c:forEach var="expr" items="${education.experiences}">
                                        <jsp:useBean id="expr" type="com.kuzmin.model.Experience"/>
                                        <input type="text" name="startDate" size="10" value="${expr.startDate}"></br>
                                        <input type="text" name="endDate" size="10" value="<%=expr.getEndDate().equals(YearMonth.now()) ? "по наст. врем" : expr.getEndDate()%>"></br>
                                        <input type="text" name="position" size="30" value="${expr.position}"></br>
                                        <textarea rows="5" cols="40">${expr.description}</textarea></br>
                                    </c:forEach>
                                </c:forEach>
                            </dd>
                        </dl>
                    </c:when>
                    <c:otherwise>
                        <dl>
                            <dt>${section.title}</dt>
                            <dd><textarea rows="10" cols="40">${resume.getSection(section)}</textarea></dd>
                        </dl>
                    </c:otherwise>
                </c:choose>
            </c:forEach>
        </p>
        <br>
        <button type="submit">Сохранить</button>
        <button onclick="window.history.back()">Отменить</button>
    </form>
</section>
<%@ include file="fragments/footer.jsp" %>
</body>
</html>

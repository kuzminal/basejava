<%@ page import="com.kuzmin.model.ContactType" %>
<%@ page import="com.kuzmin.model.OrganizationSection" %>
<%@ page import="com.kuzmin.model.SectionType" %>
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
                                <c:if test="<%=(resume.getSection(SectionType.EXPERIENCE)) != null %>">
                                    <c:forEach var="organization" varStatus="index" items="<%=((OrganizationSection)resume.getSection(SectionType.EXPERIENCE)).getOrganizations()%>">
                                        <jsp:useBean id="organization" type="com.kuzmin.model.Organization"/>
                                        <input type="hidden" name="${section}" value="true">
                                        <label> Учереждение: <input type="text" name="${section}description" size="50" value="${organization.title}"> </label></br>
                                        <label> URL: <input type="text" name="${section}url" size="30" value="${organization.url}"> </label></br>
                                        <c:if test="${organization.experiences != null}">
                                            <c:forEach var="experiences" items="${organization.experiences}">
                                                <jsp:useBean id="experiences" type="com.kuzmin.model.Experience"/>
                                                <label> Дата начала:<input type="text" name="${section}${index.index}startDate" size="10" value="${experiences.startDate}" placeholder="2019-10" pattern="\d{4}\-\d{2}"></label></br>
                                                <label> Финальная дата: <input type="text" name="${section}${index.index}endDate" size="10" placeholder="2019-10" value="${experiences.endDate}" pattern="\d{4}\-\d{2}"></label></br>
                                                <label> Позиция:  <input type="text" name="${section}${index.index}position" size="30" value="${experiences.position}"></label></br>
                                                <label> Описание: <textarea rows="5" cols="40" name="${section}${index.index}dscr">${experiences.description}</textarea></label></br>
                                            </c:forEach>
                                        </c:if>
                                    </c:forEach>
                                </c:if>
                            </dd>
                        </dl>
                    </c:when>
                    <c:when test="${section == 'EDUCATION'}">
                        <dl>
                            <dt>${section.title}</dt>
                            <dd>
                                <c:if test="<%=(resume.getSection(SectionType.EDUCATION)) != null %>">
                                    <c:forEach var="education" varStatus="index" items="<%=((OrganizationSection)resume.getSection(SectionType.EDUCATION)).getOrganizations()%>">
                                        <jsp:useBean id="education" type="com.kuzmin.model.Organization"/>
                                        <input type="hidden" name="${section}" value="true">
                                        <label> Учереждение: <input type="text" name="${section}description" size="50" value="${education.title}"> </label></br>
                                        <label> URL: <input type="text" name="${section}url" size="30" value="${education.url}"> </label></br>
                                        <c:if test="${education.experiences != null}">
                                            <c:forEach var="expr" items="${education.experiences}">
                                                <jsp:useBean id="expr" type="com.kuzmin.model.Experience"/>
                                                <label> Дата начала: <input type="text" name="${section}${index.index}startDate" size="10" value="${expr.startDate}" placeholder="2019-10" pattern="\d{4}\-\d{2}"></label></br>
                                                <label> Финальная дата: <input type="text" name="${section}${index.index}endDate" size="10" placeholder="2019-10" value="${expr.endDate}" pattern="\d{4}\-\d{2}"></label></br>
                                                <label> Квалификация\степень: <input type="text" name="${section}${index.index}position" size="30" value="${expr.position}"></label></br>
                                                <label> Описание:  <textarea rows="5" cols="40" name="${section}${index.index}dscr">${expr.description}</textarea></label></br>
                                            </c:forEach>
                                        </c:if>
                                    </c:forEach>
                                </c:if>
                            </dd>
                        </dl>
                    </c:when>
                    <c:otherwise>
                        <dl>
                            <dt>${section.title}</dt>
                            <dd><textarea rows="10" cols="40" name="${section.name()}">${resume.getSection(section)}</textarea></dd>
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

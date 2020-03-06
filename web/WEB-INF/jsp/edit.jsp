<%@ page import="com.kuzmin.model.ContactType" %>
<%@ page import="com.kuzmin.model.OrganizationSection" %>
<%@ page import="com.kuzmin.model.SectionType" %>
<%@ page import="java.time.YearMonth" %>
<%@ page import="java.util.stream.Collectors" %>
<%@ page import="java.util.Map" %><%--
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
        <input type="hidden" name="postAction" value="saveResume">
        <dl>
            <dt>Имя:</dt>
            <dd><input type="text" name="fullName" size="50" value="${resume.fullName}"></dd>
        </dl>
        <h3>Контакты <a href="resume?uuid=${resume.uuid}&action=newContact"><img src="img/add.png"></a></h3>
        <p>
            <c:set var="contacts" value="${resume.contacts}"/>
            <c:if test="${contacts != null}">
                <c:forEach var="type"
                           items="<%=resume.getContacts().entrySet().stream().filter(f -> f.getValue() != null).map(Map.Entry::getKey).collect(Collectors.toList())%>">
                    <dl>
                        <dt>${type.title}</dt>
                        <dd><input type="text" name="${type.name()}" size="30" value="${resume.getContact(type)}"> <a
                                href="resume?uuid=${resume.uuid}&contact=${type}&action=deleteContact"><img
                                src="img/delete.png"></a></dd>
                    </dl>
                </c:forEach>
            </c:if>
        </p>
        <h3>Секции <a href="resume?uuid=${resume.uuid}&action=addSection"><img src="img/add.png"></a></h3>
        <p>
            <c:forEach var="section"
                       items="<%=resume.getSections().entrySet().stream().filter(f -> f.getValue() != null).map(Map.Entry::getKey).collect(Collectors.toList())%>">
                <c:set var="sec" value="${resume.getSection(section)}"/>
                <jsp:useBean id="sec" type="com.kuzmin.model.AbstractSection"/>
            <c:choose>
            <c:when test="${section == 'EXPERIENCE' || section == 'EDUCATION'}">

        <dl>
            <dt>${section.title} <a href="resume?uuid=${resume.uuid}&sectionType=${section}&action=deleteSection"><img
                    src="img/delete.png"></a><br><br>
            </dt>
            <dd>
                <c:if test="<%=sec != null%>">
                    <c:choose>
                        <c:when test="<%=((OrganizationSection) sec).getOrganizations().size() > 0%>">
                            <c:forEach var="organization" varStatus="index"
                                       items="<%=((OrganizationSection) sec).getOrganizations()%>">
                                <jsp:useBean id="organization" type="com.kuzmin.model.Organization"/>
                                <input type="hidden" name="${section}" value="true">
                                <dl>
                                    <dt>Учереждение:</dt>
                                    <dd><input type="text" name="${section}description" size="50"
                                               value="${organization.title}"></dd>
                                    <dd>
                                        <a href="resume?uuid=${resume.uuid}&sectionType=${section}&organisation=${organization.title}&action=deleteOrganisation"><img
                                                src="img/delete.png"></a></dd>
                                </dl>
                                <dl>
                                    <dt>URL:</dt>
                                    <dd><input type="text" name="${section}url" size="30" value="${organization.url}">
                                    </dd>
                                </dl>
                                </br>
                                <c:choose>
                                    <c:when test="${organization.experiences != null}">
                                        <c:forEach var="experiences" items="${organization.experiences}">
                                            <jsp:useBean id="experiences" type="com.kuzmin.model.Experience"/>
                                            <dl>
                                                <dt>Дата начала:</dt>
                                                <dd><input type="text" name="${section}${index.index}startDate"
                                                           size="10" value="${experiences.startDate}"
                                                           placeholder="2019-10" pattern="\d{4}\-\d{2}"></dd>
                                                <dd>
                                                    <a href="resume?uuid=${resume.uuid}&sectionType=${section}&organisation=${organization.title}&expstart=${experiences.startDate}&expend=${experiences.endDate}&position=${experiences.position}&action=deleteExperience"><img
                                                            src="img/delete.png"></a></dd>
                                            </dl>
                                            <dl>
                                                <dt>Финальная дата:</dt>
                                                <dd><input type="text"
                                                           name="${section}${index.index}endDate"
                                                           size="10" placeholder="2019-10"
                                                           value="${experiences.endDate}"
                                                           pattern="\d{4}\-\d{2}"></dd>
                                            </dl>
                                            <dl>
                                                <dt>Позиция:</dt>
                                                <dd><input type="text" name="${section}${index.index}position"
                                                           size="30" value="${experiences.position}"></dd>
                                            </dl>
                                            <dl>
                                                <dt>Описание:</dt>
                                                <dd><textarea rows="5" cols="40"
                                                              name="${section}${index.index}dscr">${experiences.description}</textarea>
                                                </dd>
                                            </dl>
                                            <br>
                                        </c:forEach>
                                        <c:choose>
                                            <c:when test="${section == 'EXPERIENCE'}">
                                                <a href="resume?uuid=${resume.uuid}&sectionType=${section}&organisation=${organization.title}&action=addPosition"><img
                                                        src="img/add.png">Добавить еще опыт</a><br>
                                            </c:when>
                                            <c:when test="${section == 'EDUCATION'}">
                                                <a href="resume?uuid=${resume.uuid}&sectionType=${section}&organisation=${organization.title}&action=addPosition"><img
                                                        src="img/add.png">Добавить период обучения</a><br>
                                            </c:when>
                                        </c:choose>
                                    </c:when>
                                    <c:otherwise>
                                        <c:when test="${section == 'EXPERIENCE'}">
                                            <a href="resume?uuid=${resume.uuid}&sectionType=${section}&organisation=${organization.title}&action=addPosition"><img
                                                    src="img/add.png">Добавить еще опыт</a><br>
                                        </c:when>
                                        <c:when test="${section == 'EDUCATION'}">
                                            <a href="resume?uuid=${resume.uuid}&sectionType=${section}&organisation=${organization.title}&action=addPosition"><img
                                                    src="img/add.png">Добавить период обучения</a><br>
                                        </c:when>
                                    </c:otherwise>
                                </c:choose>
                            </c:forEach>
                        </c:when>
                        <c:otherwise>
                            <input type="hidden" name="${section}" value="true">
                            <c:choose>
                                <c:when test="${section == 'EXPERIENCE'}">
                                    <a href="resume?uuid=${resume.uuid}&sectionType=${section}&action=newSection">Добавить
                                        организацию<img src="img/add.png"></a>
                                </c:when>
                                <c:when test="${section == 'EDUCATION'}">
                                    <a href="resume?uuid=${resume.uuid}&sectionType=${section}&action=newSection">Добавить
                                        учебное заведение<img src="img/add.png"></a>
                                </c:when>
                            </c:choose>
                        </c:otherwise>
                    </c:choose>
                </c:if>
            </dd>

        </dl>
        </c:when>
        <c:otherwise>
            <dl>
                <dt>${section.title}</dt>
                <dd>
                    <c:choose>
                        <c:when test="<%=sec.toString() != null %>">
                            <textarea rows="10" cols="40"
                                      name="${section.name()}">${resume.getSection(section)}</textarea>
                        </c:when>
                        <c:otherwise>
                            <textarea rows="10" cols="40"
                                      name="${section.name()}"></textarea>
                        </c:otherwise>
                    </c:choose>
                </dd>
                <dd><a href="resume?uuid=${resume.uuid}&sectionType=${section}&action=deleteSection"><img
                        src="img/delete.png"></a></dd>
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
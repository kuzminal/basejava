<%--
  Created by IntelliJ IDEA.
  User: kuzmi
  Date: 28.02.2020
  Time: 21:24
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
    <form method="post" action="resume?uuid=${resume.uuid}&action=edit" enctype="application/x-www-form-urlencoded">
        <input type="hidden" name="${section}" value="true">
        <dl>
            <dt>Имя:</dt>
            <dd>${resume.fullName}</dd>
        </dl>
        <h3>Добавление секции</h3>
        <p>
            <label> Учереждение: <input type="text" name="${section}description" size="50" value="${organization.title}"></label></br>
            <label> URL: <input type="text" name="${section}url" size="30" value="${organization.url}"> </label></br>
            <c:choose>
                <c:when test="${organization.experiences != null}">
                    <c:forEach var="experiences" items="${organization.experiences}">
                        <jsp:useBean id="experiences" type="com.kuzmin.model.Experience"/>
                        <label> Дата начала:<input type="text" name="${section}${index.index}startDate" size="10" value="${experiences.startDate}" placeholder="2019-10" pattern="\d{4}\-\d{2}"></label></br>
                        <label> Финальная дата: <input type="text" name="${section}${index.index}endDate" size="10" placeholder="2019-10" value="${experiences.endDate}" pattern="\d{4}\-\d{2}"></label></br>
                        <label> Позиция: <input type="text" name="${section}${index.index}position" size="30" value="${experiences.position}"></label></br>
                        <label> Описание: <textarea rows="5" cols="40" name="${section}${index.index}dscr">${experiences.description}</textarea></label></br>
                    </c:forEach>
                </c:when>
                <c:otherwise>
                    <a href="resume?uuid=${resume.uuid}&action=newSection">Добавить еще <img src="img/add.png"></a>
                </c:otherwise>
            </c:choose>
        </p>
        <br>
        <button type="submit">Сохранить</button>
        <button onclick="window.history.back()">Отменить</button>
    </form>
</section>
<%@ include file="fragments/footer.jsp" %>
</body>
</html>
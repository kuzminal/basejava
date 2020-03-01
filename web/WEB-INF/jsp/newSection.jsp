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
    <jsp:useBean id="sectionType" type="com.kuzmin.model.SectionType" scope="request"/>
    <title>Резюме ${resume.fullName}</title>
</head>
<body>
<%@ include file="fragments/header.jsp" %>
<section>
    <form method="post" action="resume?uuid=${resume.uuid}&action=edit" enctype="application/x-www-form-urlencoded">
        <input type="hidden" name="sectionType" value="${sectionType}">
        <input type="hidden" name="postAction" value="saveSection">
        <dl>
            <dt>Имя:</dt>
            <dd>${resume.fullName}</dd>
        </dl>
        <h3>Добавить ${sectionType.title}</h3>
        <p>
        <dl>
            <dt>Учереждение:</dt>
            <dd><input type="text" name="description" size="50"></dd>
        </dl>
        <dl>
            <dt>URL:</dt>
            <dd><input type="text" name="url" size="30"></dd>
        </dl>
        </p>
        <p>
        <dl>
            <dt>Дата начала:</dt>
            <dd>
                <input type="text" name="startDate" size="10"
                       value="" placeholder="2019-10"
                       pattern="\d{4}\-\d{2}"></dd>
        </dl>
        <dl>
            <dt>Финальная дата:</dt>
            <dd>
                <input type="text" name="endDate" size="10"
                       placeholder="2019-10" value=""
                       pattern="\d{4}\-\d{2}"></dd>
        </dl>
        <dl>
            <dt>Позиция:</dt>
            <dd>
                <input type="text" name="position" size="30"
                       value=""></dd>
        </dl>
        <dl>
            <dt>Описание:</dt>
            <dd>
                    <textarea rows="5" cols="40"
                              name="dscr"></textarea></dd>
        </dl>
        </p>
        <br>
        <button type="submit">Сохранить</button>
        <button onclick="window.history.back()">Отменить</button>
    </form>
</section>
<%@ include file="fragments/footer.jsp" %>
</body>
</html>
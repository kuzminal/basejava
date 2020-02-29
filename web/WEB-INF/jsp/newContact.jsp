<%@ page import="com.kuzmin.model.ContactType" %><%--
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
        <input type="hidden" name="uuid" value="${resume.uuid}">
        <input type="hidden" name="postAction" value="saveContact">
        <dl>
            <dt>Имя:</dt>
            <dd>${resume.fullName}</dd>
        </dl>
        <h3>Добавление контакта</h3>
        <p>
        <dl>
            <dt>
                <select name="contactType">
                    <c:forEach items="<%=ContactType.values()%>" var="item">
                        <option value='${item.name()}'>${item.title}</option>
                    </c:forEach>
                </select>
            </dt>
            <dd><input type="text" name="contactValue" size="30" value="${resume.getContact(type)}"></dd>
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
</body>
</html>

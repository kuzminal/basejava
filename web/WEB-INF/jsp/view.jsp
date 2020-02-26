<%--
  Created by IntelliJ IDEA.
  User: aleksejkuzmin
  Date: 26.02.2020
  Time: 10:27
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
    <h2>${resume.fullName}&nbsp;<a href="resume?uuid=${resume.uuid}&action=edit">Edit</a></h2>
    <h3>Контакты</h3>
    <p>
        <c:forEach var="contactEntry" items="${resume.contacts}">
            <jsp:useBean id="contactEntry" type="java.util.Map.Entry<com.kuzmin.model.ContactType, java.lang.String>"/>
            <%=contactEntry.getKey().toHTML(contactEntry.getValue())%><br/>
        </c:forEach>
    </p>
    <c:forEach var="sectionEntry" items="${resume.sections}">
        <jsp:useBean id="sectionEntry" type="java.util.Map.Entry<com.kuzmin.model.SectionType, com.kuzmin.model.AbstractSection>"/>
        <h3><%=sectionEntry.getKey().getTitle()%></h3>
        <%=sectionEntry.getValue()%><br/>
    </c:forEach>
</section>
<%@ include file="fragments/footer.jsp" %>
</body>
</html>

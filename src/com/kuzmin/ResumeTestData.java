package com.kuzmin;

import com.kuzmin.model.*;

import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ResumeTestData {
    public static void main(String[] args) {
        System.out.println(fillResume("uuid123", "Ivanov Ivan Ivanovich"));
    }

    public static Resume fillResume(String uuid, String fullname) {
        Resume resume = new Resume(uuid, fullname);
        String phone = "+7(123)4567890";
        String email = "mail@gmail.com";
        String icq = "123456789";
        String git = "github.com";
        Map<ContactType, String> contacts = resume.getContacts();
        contacts.put(ContactType.PHONE, phone);
        contacts.put(ContactType.ICQ, icq);
        contacts.put(ContactType.EMAIL, email);
        contacts.put(ContactType.GIT, git);
        resume.setContacts(contacts);
        Map<SectionType, AbstractSection> sections = resume.getSections();
        AbstractSection objective = new TextSection("Ведущий стажировок и корпоративного обучения по Java Web и Enterprise технологиям");
        sections.put(SectionType.OBJECTIVE, objective);
        AbstractSection personal = new TextSection("Аналитический склад ума, сильная логика, креативность, инициативность. Пурист кода и архитектуры.");
        sections.put(SectionType.PERSONAL, personal);
        List<String> achievements = new ArrayList<>();
        achievements.add("С 2013 года: разработка проектов \"Разработка Web приложения\",\"Java Enterprise\", \"Многомодульный maven. Многопоточность. XML (JAXB/StAX). Веб сервисы (JAX-RS/SOAP). Удаленное взаимодействие (JMS/AKKA)\". Организация онлайн стажировок и ведение проектов. Более 1000 выпускников.");
        achievements.add("Реализация двухфакторной аутентификации для онлайн платформы управления проектами Wrike. Интеграция с Twilio, DuoSecurity, Google Authenticator, Jira, Zendesk.");
        achievements.add("Налаживание процесса разработки и непрерывной интеграции ERP системы River BPM. Интеграция с 1С, Bonita BPM, CMIS, LDAP. Разработка приложения управления окружением на стеке: Scala/Play/Anorm/JQuery. Разработка SSO аутентификации и авторизации различных ERP модулей, интеграция CIFS/SMB java сервера.");
        AbstractSection achievementsSection = new TextListSection(achievements);
        sections.put(SectionType.ACHIEVEMENT, achievementsSection);
        List<String> qualifications = new ArrayList<>();
        qualifications.add("JEE AS: GlassFish (v2.1, v3), OC4J, JBoss, Tomcat, Jetty, WebLogic, WSO2");
        qualifications.add("Version control: Subversion, Git, Mercury, ClearCase, Perforce");
        qualifications.add("DB: PostgreSQL(наследование, pgplsql, PL/Python), Redis (Jedis), H2, Oracle,");
        AbstractSection qualification = new TextListSection(qualifications);
        sections.put(SectionType.QUALIFICATIONS, qualification);
        List<Experience> experiencesJavaOnline = new ArrayList<>();
        List<Organization> organizations = new ArrayList<>();
        Experience author = new Experience(YearMonth.parse("2013-10"), YearMonth.now(), "Создание, организация и проведение Java онлайн проектов и стажировок.", "Автор проекта");
        experiencesJavaOnline.add(author);
        Organization javaOnline = new Organization("Java Online Projects", "http://javaops.com", experiencesJavaOnline);
        organizations.add(javaOnline);
        Experience seniorDeveloper = new Experience(YearMonth.parse("2014-10"), YearMonth.parse("2016-01"), "Проектирование и разработка онлайн платформы управления проектами Wrike (Java 8 API, Maven, Spring, MyBatis, Guava, Vaadin, PostgreSQL, Redis). Двухфакторная аутентификация, авторизация по OAuth1, OAuth2, JWT SSO.", "Старший разработчик (backend)");
        Experience director = new Experience(YearMonth.parse("2016-01"), YearMonth.parse("2018-01"), "Руководство компание для достижения светлого будущего", "Директор компании");
        List<Experience> wrikeExperience = new ArrayList<>();
        wrikeExperience.add(seniorDeveloper);
        wrikeExperience.add(director);
        Organization seniorDeveloperOrganization = new Organization("Wrike","https://wrike.com", wrikeExperience);
        organizations.add(seniorDeveloperOrganization);
        OrganizationSection experience = new OrganizationSection(organizations);
        sections.put(SectionType.EXPERIENCE, experience);

        List<Organization> educations = new ArrayList<>();
        List<Experience> educationCoursera = new ArrayList<>();
        Experience courseraExperience = new Experience(YearMonth.parse("2013-03"), YearMonth.parse("2013-05"), "\"Functional Programming Principles in Scala\" by Martin Odersky", "");
        educationCoursera.add(courseraExperience);
        Organization coursera = new Organization("Coursera", "https://coursera.com", educationCoursera);
        educations.add(coursera);
        List<Experience> educationMPTU = new ArrayList<>();
        Experience mptuExperience = new Experience(YearMonth.parse("1984-09"), YearMonth.parse("1987-06"), "Закончил с отличием", "");
        educationMPTU.add(mptuExperience);
        Organization mptu = new Organization("Заочная физико-техническая школа при МФТИ", "http://mspu.ru", educationMPTU);
        educations.add(mptu);
        OrganizationSection education = new OrganizationSection(educations);
        sections.put(SectionType.EDUCATION, education);

        resume.setSections(sections);
        return resume;
    }
}

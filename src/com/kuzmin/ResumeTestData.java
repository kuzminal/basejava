package com.kuzmin;

import com.kuzmin.model.*;

import java.time.YearMonth;
import java.util.*;

public class ResumeTestData {
    public static void main(String[] args) {
        Resume resume = new Resume("Ivanov Ivan");
        Contact phone = new Contact("+7(123)4567890");
        Contact email = new Contact("mail@gmail.com");
        Contact icq = new Contact("123456789");
        Contact git = new Contact("github.com");
        EnumMap<ContactType, Contact> contacts = resume.getContacts();
        contacts.put(ContactType.PHONE, phone);
        contacts.put(ContactType.ICQ, icq);
        contacts.put(ContactType.EMAIL, email);
        contacts.put(ContactType.GIT, git);
        resume.setContacts(contacts);
        EnumMap<SectionType, AbstractSection> sections = resume.getSections();
        AbstractSection objective = new TextSection(SectionType.OBJECTIVE, "Ведущий стажировок и корпоративного обучения по Java Web и Enterprise технологиям");
        sections.put(SectionType.OBJECTIVE, objective);
        AbstractSection personal = new TextSection(SectionType.PERSONAL, "Аналитический склад ума, сильная логика, креативность, инициативность. Пурист кода и архитектуры.");
        sections.put(SectionType.PERSONAL, personal);
        List<String> achievements = new ArrayList<>();
        achievements.add("С 2013 года: разработка проектов \"Разработка Web приложения\",\"Java Enterprise\", \"Многомодульный maven.\n Многопоточность. XML (JAXB/StAX). Веб сервисы (JAX-RS/SOAP). Удаленное взаимодействие (JMS/AKKA)\".\n Организация онлайн стажировок и ведение проектов. Более 1000 выпускников.");
        achievements.add("Реализация двухфакторной аутентификации для онлайн платформы управления проектами Wrike. Интеграция с Twilio,\n DuoSecurity, Google Authenticator, Jira, Zendesk.");
        achievements.add("Налаживание процесса разработки и непрерывной интеграции ERP системы River BPM. Интеграция с 1С, Bonita BPM,\n CMIS, LDAP. Разработка приложения управления окружением на стеке: Scala/Play/Anorm/JQuery. Разработка SSO\n аутентификации и авторизации различных ERP модулей, интеграция CIFS/SMB java сервера.");
        AbstractSection achievementsSection = new TextListSection(SectionType.ACHIEVEMENT, achievements);
        sections.put(SectionType.ACHIEVEMENT, achievementsSection);
        List<String> qualifications = new ArrayList<>();
        qualifications.add("JEE AS: GlassFish (v2.1, v3), OC4J, JBoss, Tomcat, Jetty, WebLogic, WSO2");
        qualifications.add("Version control: Subversion, Git, Mercury, ClearCase, Perforce");
        qualifications.add("DB: PostgreSQL(наследование, pgplsql, PL/Python), Redis (Jedis), H2, Oracle,");
        AbstractSection qualification = new TextListSection(SectionType.QUALIFICATIONS, qualifications);
        sections.put(SectionType.QUALIFICATIONS, qualification);
        List<Organization> experiences = new ArrayList<>();
        experiences.add(new Organization(YearMonth.parse("2013-10"), YearMonth.now(), "Автор проекта.\nСоздание, организация и проведение Java онлайн проектов и стажировок.", "Java Online Projects"));
        experiences.add(new Organization(YearMonth.parse("2014-10"), YearMonth.parse("2016-01"), "Старший разработчик (backend)\n" +
                "Проектирование и разработка онлайн платформы управления проектами Wrike (Java 8 API, Maven, Spring, MyBatis, Guava, Vaadin, PostgreSQL, Redis). Двухфакторная аутентификация, авторизация по OAuth1, OAuth2, JWT SSO.", "Wrike"));
        OrganizationSection experience = new OrganizationSection(SectionType.EXPERIENCE, experiences);
        sections.put(SectionType.EXPERIENCE, experience);
        List<Organization> educations = new ArrayList<>();
        educations.add(new Organization(YearMonth.parse("2013-03"), YearMonth.parse("2013-05"), "\"Functional Programming Principles in Scala\" by Martin Odersky", "Coursera"));
        educations.add(new Organization(YearMonth.parse("1984-09"), YearMonth.parse("1987-06"), "Закончил с отличием", "Заочная физико-техническая школа при МФТИ"));
        resume.setSections(sections);
        System.out.println(resume);
    }
}

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
        Contact phone = new Contact("+7(123)4567890");
        Contact email = new Contact("mail@gmail.com");
        Contact icq = new Contact("123456789");
        Contact git = new Contact("github.com");
        Map<ContactType, Contact> contacts = resume.getContacts();
        contacts.put(ContactType.PHONE, phone);
        contacts.put(ContactType.ICQ, icq);
        contacts.put(ContactType.EMAIL, email);
        contacts.put(ContactType.GIT, git);
        resume.setContacts(contacts);
        Map<SectionType, AbstractSection> sections = resume.getSections();
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
        List<Experience> experiencesJavaOnline = new ArrayList<>();
        List<Organization> organizations = new ArrayList<>();
        Experience author = new Experience(YearMonth.parse("2013-10"), YearMonth.now(), "Автор проекта.\nСоздание, организация и проведение Java онлайн проектов и стажировок.");
        experiencesJavaOnline.add(author);
        Organization javaOnline = new Organization("Java Online Projects", "http://javaops.com", experiencesJavaOnline);
        organizations.add(javaOnline);
        Experience seniorDeveloper = new Experience(YearMonth.parse("2014-10"), YearMonth.parse("2016-01"), "Старший разработчик (backend).\nПроектирование и разработка онлайн платформы управления проектами Wrike (Java 8 API, Maven, Spring, MyBatis, Guava, Vaadin, PostgreSQL, Redis). \nДвухфакторная аутентификация, авторизация по OAuth1, OAuth2, JWT SSO.");
        Experience director = new Experience(YearMonth.parse("2016-01"), YearMonth.parse("2018-01"), "Директор компании.\nРуководство компание для достижения светлого будущего");
        List<Experience> wrikeExperience = new ArrayList<>();
        wrikeExperience.add(seniorDeveloper);
        wrikeExperience.add(director);
        Organization seniorDeveloperOrganization = new Organization("Wrike","https://wrike.com", wrikeExperience);
        organizations.add(seniorDeveloperOrganization);
        OrganizationSection experience = new OrganizationSection(SectionType.EXPERIENCE, organizations);
        sections.put(SectionType.EXPERIENCE, experience);

        List<Organization> educations = new ArrayList<>();
        List<Experience> educationCoursera = new ArrayList<>();
        Experience courseraExperience = new Experience(YearMonth.parse("2013-03"), YearMonth.parse("2013-05"), "\"Functional Programming Principles in Scala\" by Martin Odersky");
        educationCoursera.add(courseraExperience);
        Organization coursera = new Organization("Coursera", "https://coursera.com", educationCoursera);
        educations.add(coursera);
        List<Experience> educationMPTU = new ArrayList<>();
        Experience mptuExperience = new Experience(YearMonth.parse("1984-09"), YearMonth.parse("1987-06"), "Закончил с отличием");
        educationMPTU.add(mptuExperience);
        Organization mptu = new Organization("Заочная физико-техническая школа при МФТИ", "http://mspu.ru", educationMPTU);
        educations.add(mptu);
        OrganizationSection education = new OrganizationSection(SectionType.EDUCATION, educations);
        sections.put(SectionType.EDUCATION, education);

        resume.setSections(sections);
        return resume;
    }
}

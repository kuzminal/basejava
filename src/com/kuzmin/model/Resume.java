package com.kuzmin.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.*;

/**
 * Initial resume class
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Resume implements Comparable<Resume>, Serializable {
    private static final long serialVersionUID = 1L;
    private String uuid;
    private String fullName;
    private Map<ContactType, String> contacts = Collections.emptyMap();
    private Map<SectionType, AbstractSection> sections = Collections.emptyMap();

    public Resume(String fullName) {
        this(UUID.randomUUID().toString(), fullName);
    }

    public Resume(String uuid, String fullName) {
        this.uuid = uuid;
        this.fullName = fullName;
        this.contacts = new EnumMap<>(ContactType.class);
        this.sections = new EnumMap<>(SectionType.class);
    }

    public Resume() {
        uuid = UUID.randomUUID().toString();
        fullName = "";
    }

    public Map<ContactType, String> getContacts() {
        return contacts;
    }

    public String getContact(ContactType type) {
        return contacts.get(type);
    }

    public AbstractSection getSection(SectionType type) {
        return sections.get(type);
    }

    public void setContacts(Map<ContactType, String> contacts) {
        this.contacts = contacts;
    }

    public Map<SectionType, AbstractSection> getSections() {
        return sections;
    }

    public void setSections(Map<SectionType, AbstractSection> sections) {
        this.sections = sections;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    @Override
    public String toString() {
        StringBuilder printResume = new StringBuilder();
        printResume.append("Резюме uuid : ").append(uuid).append("\n");
        printResume.append("ФИО : ").append(fullName);
        printResume.append("\n");
        printResume.append("\nКонтакты :\n");
        for (Map.Entry contact : contacts.entrySet()) {
            printResume.append(contact.getKey()).append(" : ").append(contact.getValue().toString()).append("\n");
        }
        for (Map.Entry section : sections.entrySet()) {
            printResume.append("\n").append(section.getKey().toString()).append(" :\n");
            printResume.append(section.getValue().toString());
        }
        return printResume.toString();
    }

    @Override
    public int compareTo(Resume o) {
        int comparingResult = fullName.compareTo(o.fullName);
        return comparingResult == 0 ? uuid.compareTo(o.uuid) : fullName.compareTo(o.fullName);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Resume resume = (Resume) o;
        return Objects.equals(uuid, resume.uuid) &&
                Objects.equals(fullName, resume.fullName) &&
                Objects.equals(contacts, resume.contacts) &&
                Objects.equals(sections, resume.sections);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uuid, fullName, contacts, sections);
    }

    public void addContact(ContactType contactType, String contact) {
        contacts.put(contactType, contact);
    }

    public void addSection(SectionType sectionType, AbstractSection section) {
        sections.put(sectionType, section);
    }

    public void removeContact(ContactType contactType) {
        if (contactType != null) {
            contacts.remove(contactType);
        }
    }

    public void removeSection(SectionType sectionType) {
        if (sectionType != null) {
            sections.remove(sectionType);
        }
    }
}

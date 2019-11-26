package com.kuzmin.storage.serializer;

import com.kuzmin.exception.StorageException;
import com.kuzmin.model.*;

import java.io.*;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DataStreamSerializer implements IOStrategy {
    @Override
    public void doWrite(Resume resume, OutputStream os) {
        try {
            try (DataOutputStream dos = new DataOutputStream(os)) {
                dos.writeUTF(resume.getUuid());
                dos.writeUTF(resume.getFullName());
                Map<ContactType, Contact> contacts = resume.getContacts();
                dos.writeInt(contacts.size());
                for (Map.Entry<ContactType, Contact> entry : resume.getContacts().entrySet()) {
                    dos.writeUTF(entry.getKey().name());
                    dos.writeUTF(entry.getValue().getContact());
                }
                Map<SectionType, AbstractSection> sections = resume.getSections();
                dos.writeInt(sections.size());
                TextListSection textListSection;
                for (Map.Entry<SectionType, AbstractSection> entry : sections.entrySet()) {
                    dos.writeUTF(entry.getKey().name());
                    if (entry.getKey().equals(SectionType.PERSONAL)) {
                        dos.writeUTF(((TextSection) entry.getValue()).getTextInformation());
                    } else if (entry.getKey().equals(SectionType.OBJECTIVE)) {
                        dos.writeUTF(((TextSection) entry.getValue()).getTextInformation());
                    } else if (entry.getKey().equals(SectionType.ACHIEVEMENT)) {
                        textListSection = (TextListSection) entry.getValue();
                        dos.writeInt(textListSection.getTextInformation().size());
                        for (String text : textListSection.getTextInformation()) {
                            dos.writeUTF(text);
                        }
                    } else if (entry.getKey().equals(SectionType.QUALIFICATIONS)) {
                        textListSection = (TextListSection) entry.getValue();
                        dos.writeInt(textListSection.getTextInformation().size());
                        for (String text : textListSection.getTextInformation()) {
                            dos.writeUTF(text);
                        }
                    } else if (entry.getKey().equals(SectionType.EXPERIENCE)) {
                        OrganizationSection orgSect = (OrganizationSection) entry.getValue();
                        dos.writeInt(orgSect.getOrganizations().size());
                        for (Organization org : orgSect.getOrganizations()) {
                            dos.writeUTF(org.getTitle());
                            dos.writeUTF(org.getUrl());
                            dos.writeInt(org.getExperiences().size());
                            for (Experience exp : org.getExperiences()) {
                                dos.writeUTF(exp.getStartDate().toString());
                                dos.writeUTF(exp.getEndDate().toString());
                                dos.writeUTF(exp.getDescription());
                            }
                        }
                    } else if (entry.getKey().equals(SectionType.EDUCATION)) {
                        OrganizationSection orgSect = (OrganizationSection) entry.getValue();
                        dos.writeInt(orgSect.getOrganizations().size());
                        for (Organization org : orgSect.getOrganizations()) {
                            dos.writeUTF(org.getTitle());
                            dos.writeUTF(org.getUrl());
                            dos.writeInt(org.getExperiences().size());
                            for (Experience exp : org.getExperiences()) {
                                dos.writeUTF(exp.getStartDate().toString());
                                dos.writeUTF(exp.getEndDate().toString());
                                dos.writeUTF(exp.getDescription());
                            }
                        }
                    }
                }
            }
        } catch (
                IOException e) {
            throw new StorageException("Error with DataStream parsing", null, e);
        }

    }

    @Override
    public Resume doRead(InputStream is) {
        try (DataInputStream dis = new DataInputStream(is)) {
            Resume resume = new Resume(dis.readUTF(), dis.readUTF());
            int size = dis.readInt();
            for (int i = 0; i < size; i++) {
                resume.addContact(ContactType.valueOf(dis.readUTF()), new Contact(dis.readUTF()));
            }
            size = dis.readInt();
            for (int i = 0; i < size; i++) {
                String section = dis.readUTF();
                int sectionSize = 0;
                if (section.equals("PERSONAL")) {
                    resume.addSection(SectionType.PERSONAL, new TextSection(SectionType.PERSONAL, dis.readUTF()));
                } else if (section.equals("OBJECTIVE")) {
                    resume.addSection(SectionType.OBJECTIVE, new TextSection(SectionType.OBJECTIVE, dis.readUTF()));
                } else if (section.equals("ACHIEVEMENT")) {
                    sectionSize = dis.readInt();
                    List<String> texts = new ArrayList<>();
                    for (int j = 0; j < sectionSize; j++) {
                        texts.add(dis.readUTF());
                    }
                    resume.addSection(SectionType.ACHIEVEMENT, new TextListSection(SectionType.ACHIEVEMENT, texts));
                } else if (section.equals("QUALIFICATIONS")) {
                    sectionSize = dis.readInt();
                    List<String> texts = new ArrayList<>();
                    for (int j = 0; j < sectionSize; j++) {
                        texts.add(dis.readUTF());
                    }
                    resume.addSection(SectionType.QUALIFICATIONS, new TextListSection(SectionType.QUALIFICATIONS, texts));
                } else if (section.equals("EXPERIENCE")) {
                    sectionSize = dis.readInt();
                    List<Organization> organisations = new ArrayList<>();
                    for (int j = 0; j < sectionSize; j++) {
                        Organization organization = new Organization();
                        String title = dis.readUTF();
                        String url = dis.readUTF();
                        int expSize = dis.readInt();
                        List<Experience> expList = new ArrayList<>();
                        for (int a = 0; a < expSize; a++) {
                            YearMonth startDate = YearMonth.parse(dis.readUTF());
                            YearMonth endtDate = YearMonth.parse(dis.readUTF());
                            String description = dis.readUTF();
                            expList.add(new Experience(startDate, endtDate, description));
                        }
                        organization.setExperiences(expList);
                        organization.setTitle(title);
                        organization.setUrl(url);
                        organisations.add(organization);
                    }
                    resume.addSection(SectionType.EXPERIENCE, new OrganizationSection(SectionType.EXPERIENCE, organisations));
                } else if (section.equals("EDUCATION")) {
                    sectionSize = dis.readInt();
                    List<Organization> organisations = new ArrayList<>();
                    for (int j = 0; j < sectionSize; j++) {
                        Organization organization = new Organization();
                        String title = dis.readUTF();
                        String url = dis.readUTF();
                        int expSize = dis.readInt();
                        List<Experience> expList = new ArrayList<>();
                        for (int a = 0; a < expSize; a++) {
                            YearMonth startDate = YearMonth.parse(dis.readUTF());
                            YearMonth endtDate = YearMonth.parse(dis.readUTF());
                            String description = dis.readUTF();
                            expList.add(new Experience(startDate, endtDate, description));
                        }
                        organization.setExperiences(expList);
                        organization.setTitle(title);
                        organization.setUrl(url);
                        organisations.add(organization);
                    }
                    resume.addSection(SectionType.EDUCATION, new OrganizationSection(SectionType.EDUCATION, organisations));
                }
            }
            return resume;
        } catch (IOException e) {
            throw new StorageException("Error with DataStream parsing", null, e);
        }
    }
}

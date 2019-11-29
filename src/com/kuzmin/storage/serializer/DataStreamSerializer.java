package com.kuzmin.storage.serializer;

import com.kuzmin.model.*;

import java.io.*;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DataStreamSerializer implements IOStrategy {
    @Override
    public void doWrite(Resume resume, OutputStream os) throws IOException {
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
            for (Map.Entry<SectionType, AbstractSection> entry : sections.entrySet()) {
                dos.writeUTF(entry.getKey().name());
                SectionType sectionType = entry.getKey();
                switch (sectionType) {
                    case PERSONAL:
                    case OBJECTIVE: {
                        dos.writeUTF(((TextSection) entry.getValue()).getTextInformation());
                        break;
                    }
                    case ACHIEVEMENT:
                    case QUALIFICATIONS: {
                        TextListSection textListSection = (TextListSection) entry.getValue();
                        dos.writeInt(textListSection.getTextInformation().size());
                        for (String text : textListSection.getTextInformation()) {
                            dos.writeUTF(text);
                        }
                        break;
                    }
                    case EXPERIENCE:
                    case EDUCATION: {
                        OrganizationSection orgSect = (OrganizationSection) entry.getValue();
                        dos.writeInt(orgSect.getOrganizations().size());
                        for (Organization org : orgSect.getOrganizations()) {
                            if (org.getTitle() != null) {
                                dos.writeUTF(org.getTitle());
                            } else {
                                dos.writeUTF("");
                            }
                            if (org.getUrl() != null) {
                                dos.writeUTF(org.getUrl());
                            } else {
                                dos.writeUTF("");
                            }
                            dos.writeInt(org.getExperiences().size());
                            for (Experience exp : org.getExperiences()) {
                                dos.writeUTF(exp.getStartDate().toString());
                                dos.writeUTF(exp.getEndDate().toString());
                                dos.writeUTF(exp.getDescription());
                                dos.writeUTF(exp.getPosition());
                            }
                        }
                        break;
                    }
                }
            }
        }
    }

    @Override
    public Resume doRead(InputStream is) throws IOException {
        try (DataInputStream dis = new DataInputStream(is)) {
            Resume resume = new Resume(dis.readUTF(), dis.readUTF());
            int size = dis.readInt();
            for (int i = 0; i < size; i++) {
                resume.addContact(ContactType.valueOf(dis.readUTF()), new Contact(dis.readUTF()));
            }
            size = dis.readInt();
            for (int i = 0; i < size; i++) {
                String section = dis.readUTF();
                SectionType sectionType = SectionType.valueOf(section);
                switch (sectionType) {
                    case PERSONAL:
                    case OBJECTIVE: {
                        resume.addSection(sectionType, new TextSection(sectionType, dis.readUTF()));
                        break;
                    }
                    case ACHIEVEMENT:
                    case QUALIFICATIONS: {
                        int sectionSize = dis.readInt();
                        List<String> texts = new ArrayList<>();
                        for (int j = 0; j < sectionSize; j++) {
                            texts.add(dis.readUTF());
                        }
                        resume.addSection(sectionType, new TextListSection(sectionType, texts));
                        break;
                    }
                    case EXPERIENCE:
                    case EDUCATION: {
                        int sectionSize = dis.readInt();
                        List<Organization> organisations = new ArrayList<>();
                        for (int j = 0; j < sectionSize; j++) {
                            Organization organization = new Organization();
                            String title = dis.readUTF();
                            String url = dis.readUTF();
                            int expSize = dis.readInt();
                            List<Experience> expList = new ArrayList<>();
                            for (int a = 0; a < expSize; a++) {
                                YearMonth startDate = YearMonth.parse(dis.readUTF());
                                YearMonth endDate = YearMonth.parse(dis.readUTF());
                                String description = dis.readUTF();
                                String position = dis.readUTF();
                                expList.add(new Experience(startDate, endDate, description, position));
                            }
                            organization.setExperiences(expList);
                            if (!title.equals("")) {
                                organization.setTitle(title);
                            }
                            if (!url.equals("")) {
                                organization.setUrl(url);
                            }
                            organisations.add(organization);
                        }
                        resume.addSection(sectionType, new OrganizationSection(sectionType, organisations));
                        break;
                    }
                }
            }
            return resume;
        }
    }
}

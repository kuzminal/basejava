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
                            dos.writeUTF(org.getTitle());
                            dos.writeUTF(org.getUrl());
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
        } catch (IOException e) {
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
                SectionType sectionType = SectionType.valueOf(section);
                int sectionSize = 0;
                switch (sectionType) {
                    case PERSONAL:
                    case OBJECTIVE: {
                        resume.addSection(sectionType, new TextSection(sectionType, dis.readUTF()));
                        break;
                    }
                    case ACHIEVEMENT:
                    case QUALIFICATIONS: {
                        sectionSize = dis.readInt();
                        List<String> texts = new ArrayList<>();
                        for (int j = 0; j < sectionSize; j++) {
                            texts.add(dis.readUTF());
                        }
                        resume.addSection(sectionType, new TextListSection(sectionType, texts));
                        break;
                    }
                    case EXPERIENCE:
                    case EDUCATION: {
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
                                String position = dis.readUTF();
                                expList.add(new Experience(startDate, endtDate, description, position));
                            }
                            organization.setExperiences(expList);
                            organization.setTitle(title);
                            organization.setUrl(url);
                            organisations.add(organization);
                        }
                        resume.addSection(sectionType, new OrganizationSection(sectionType, organisations));
                        break;
                    }
                }
            }
            return resume;
        } catch (IOException e) {
            throw new StorageException("Error with DataStream parsing", null, e);
        }
    }
}
package com.kuzmin.storage.serializer;

import com.kuzmin.model.*;

import java.io.*;
import java.time.YearMonth;
import java.util.*;

public class DataStreamSerializer implements IOStrategy {
    @Override
    public void doWrite(Resume resume, OutputStream os) throws IOException {
        try (DataOutputStream dos = new DataOutputStream(os)) {
            dos.writeUTF(resume.getUuid());
            dos.writeUTF(resume.getFullName());
            Map<ContactType, Contact> contacts = resume.getContacts();
            customWtiteForEach(resume.getContacts().entrySet(), dos, collectionElement -> {
                dos.writeUTF(collectionElement.getKey().name());
                dos.writeUTF(collectionElement.getValue().getContact());
            });
            Map<SectionType, AbstractSection> sections = resume.getSections();
            customWtiteForEach(sections.entrySet(), dos, entry -> {
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
                        customWtiteForEach(textListSection.getTextInformation(), dos, dos::writeUTF);
                        textListSection.getTextInformation().forEach(System.out::println);
                        break;
                    }
                    case EXPERIENCE:
                    case EDUCATION: {
                        OrganizationSection orgSect = (OrganizationSection) entry.getValue();
                        customWtiteForEach(orgSect.getOrganizations(), dos, org -> {
                            dos.writeUTF(org.getTitle());
                            if (org.getUrl() != null) {
                                dos.writeUTF(org.getUrl());
                            } else {
                                dos.writeUTF("");
                            }
                            customWtiteForEach(org.getExperiences(), dos, exp -> {
                                dos.writeUTF(exp.getStartDate().toString());
                                dos.writeUTF(exp.getEndDate().toString());
                                if (exp.getDescription() != null) {
                                    dos.writeUTF(exp.getDescription());
                                } else {
                                    dos.writeUTF("");
                                }
                                dos.writeUTF(exp.getPosition());
                            });
                        });
                        break;
                    }
                }
            });
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

    <T> void customWtiteForEach(Collection<T> collection, DataOutputStream dos, WriteEachElement<T> action) throws IOException {
        Objects.requireNonNull(action);
        Objects.requireNonNull(collection);
        dos.writeInt(collection.size());
        for (T t : collection) {
            action.accept(t);
        }
    }

    <T> void customReadForEach(DataInputStream dis, ReadEachElement<T> action) throws IOException {
        Objects.requireNonNull(action);
        int size = dis.readInt();
        for (int i = 0; i < size; i++) {
            action.accept();
        }
    }
}

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
            customWtiteForEach(resume.getContacts().entrySet(), dos, collectionElement -> {
                dos.writeUTF(collectionElement.getKey().name());
                dos.writeUTF(collectionElement.getValue());
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
            customReadForEach(dis, () -> resume.addContact(ContactType.valueOf(dis.readUTF()), dis.readUTF()));
            customReadForEach(dis, () -> {
                String section = dis.readUTF();
                SectionType sectionType = SectionType.valueOf(section);
                switch (sectionType) {
                    case PERSONAL:
                    case OBJECTIVE: {
                        resume.addSection(sectionType, new TextSection(dis.readUTF()));
                        break;
                    }
                    case ACHIEVEMENT:
                    case QUALIFICATIONS: {
                        List<String> texts = new ArrayList<>();
                        customReadForEach(dis, () -> {
                            texts.add(dis.readUTF());
                        });
                        resume.addSection(sectionType, new TextListSection(texts));
                        break;
                    }
                    case EXPERIENCE:
                    case EDUCATION: {
                        List<Organization> organisations = new ArrayList<>();
                        customReadForEach(dis, () -> {
                            Organization organization = new Organization();
                            String title = dis.readUTF();
                            String url = dis.readUTF();
                            List<Experience> expList = new ArrayList<>();
                            customReadForEach(dis, () -> {
                                YearMonth startDate = YearMonth.parse(dis.readUTF());
                                YearMonth endDate = YearMonth.parse(dis.readUTF());
                                String description = dis.readUTF();

                                if (description.equals("")) {
                                    description = null;
                                }

                                String position = dis.readUTF();
                                expList.add(new Experience(startDate, endDate, description, position));
                            });
                            organization.setExperiences(expList);
                            organization.setTitle(title);

                            if (!url.equals("")) {
                                organization.setUrl(url);
                            }

                            organisations.add(organization);
                        });
                        resume.addSection(sectionType, new OrganizationSection(organisations));
                        break;
                    }
                }
            });

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

    void customReadForEach(DataInputStream dis, ReadEachElement action) throws IOException {
        Objects.requireNonNull(action);
        int size = dis.readInt();
        for (int i = 0; i < size; i++) {
            action.accept();
        }
    }
}

package com.kuzmin.storage.serializer;

import com.kuzmin.exception.StorageException;
import com.kuzmin.model.Contact;
import com.kuzmin.model.ContactType;
import com.kuzmin.model.Resume;

import java.io.*;
import java.util.Map;

public class DataStreamSerializer implements IOStrategy{
    @Override
    public void doWrite(Resume resume, OutputStream os) {
        try {
            try (DataOutputStream dos = new DataOutputStream(os)) {
                dos.writeUTF(resume.getUuid());
                dos.writeUTF(resume.getFullName());
                Map<ContactType, Contact> contacts = resume.getContacts();
                dos.writeInt(contacts.size());
                for (Map.Entry<ContactType, Contact> entry : resume.getContacts().entrySet()){
                    dos.writeUTF(entry.getKey().name());
                    dos.writeUTF(entry.getValue().getContact());
                }
            }
        } catch (IOException e) {
            throw new StorageException("Error with DataStream parsing", null, e);
        }
    }

    @Override
    public Resume doRead(InputStream is) {
        try (DataInputStream dis = new DataInputStream(is)){
            Resume resume = new Resume(dis.readUTF(), dis.readUTF());
            int size = dis.readInt();
            for (int i=0; i < size; i ++){
                resume.addContact(ContactType.valueOf(dis.readUTF()), new Contact(dis.readUTF()));
            }
            return resume;
        } catch (IOException e) {
            throw new StorageException("Error with DataStream parsing", null, e);
        }
    }
}

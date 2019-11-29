package com.kuzmin.storage.serializer;

import com.kuzmin.exception.StorageException;
import com.kuzmin.model.Resume;

import java.io.*;

public class ObjectStreamStrategy implements IOStrategy {

    public void doWrite(Resume resume, OutputStream os) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(os)) {
            oos.writeObject(resume);
        }
    }

    public Resume doRead(InputStream is) throws IOException {
        try (ObjectInputStream ois = new ObjectInputStream(is)) {
            return (Resume) ois.readObject();
        } catch (ClassNotFoundException e) {
            throw new StorageException("Error with XML parsing", null, e);
        }
    }
}

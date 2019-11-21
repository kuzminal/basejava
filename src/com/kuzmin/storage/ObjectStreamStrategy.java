package com.kuzmin.storage;

import com.kuzmin.exception.StorageException;
import com.kuzmin.model.Resume;

import java.io.*;

public class ObjectStreamStrategy implements WorkingStrategy {

    public void doWrite(Resume resume, Object os) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream((OutputStream) os)) {
            oos.writeObject(resume);
        }
    }

    public Resume doRead(Object is) throws IOException {
        try (ObjectInputStream ois = new ObjectInputStream((InputStream) is)) {
            return (Resume) ois.readObject();
        } catch (ClassNotFoundException e) {
            throw new StorageException("Error read resume", null, e);
        }
    }
}

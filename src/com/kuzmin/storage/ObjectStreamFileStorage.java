package com.kuzmin.storage;

import com.kuzmin.exception.StorageException;
import com.kuzmin.model.Resume;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ObjectStreamFileStorage extends AbstractStorage<File> {
    private File directory;
    WorkingStrategy workingStrategy;

    protected ObjectStreamFileStorage(File directory) {
        Objects.requireNonNull(directory, "directory must not be null");
        if (!directory.isDirectory() || !directory.canWrite()) {
            throw new IllegalArgumentException(directory.toString() + " is not directory or is not writable");
        }
        this.directory = directory;
    }

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

    protected void updateResume(Resume resume, File file) {
        try {
            doWrite(resume, new FileOutputStream(file));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected void saveResume(Resume resume, File file) {
        try {
            file.createNewFile();
            doWrite(resume, new FileOutputStream(file));
        } catch (IOException e) {
            throw new StorageException("IO error", file.getAbsolutePath(), e);
        }
    }

    protected void deleteResume(File file) {
        file.delete();
    }

    protected File getSearchKey(String file) {
        return new File(directory, file);
    }

    protected boolean checkSearchKey(File file) {
        return file.exists();
    }

    protected Resume getResume(File file) {
        try {
            return doRead(new FileInputStream(file));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    protected List<Resume> getAll() {
        List<Resume> resumes = new ArrayList<>();
        if (directory.listFiles() != null) {
            for (File file : directory.listFiles()) {
                if (!file.isDirectory()) {
                    try {
                        resumes.add(doRead(new FileInputStream(file)));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return resumes;
    }

    public void clear() {
        if (directory.listFiles() != null) {
            for (File file : directory.listFiles()) {
                if (!file.isDirectory()) {
                    file.delete();
                }
            }
        }
    }

    public int size() {
        int count = 0;
        if (directory.listFiles() != null) {
            for (File file : directory.listFiles()) {
                if (!file.isDirectory()) {
                    count++;
                }
            }
        }
        return count;
    }
}

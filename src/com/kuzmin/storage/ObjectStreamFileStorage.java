package com.kuzmin.storage;

import com.kuzmin.exception.StorageException;
import com.kuzmin.model.Resume;
import com.kuzmin.storage.serializer.IOStrategy;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ObjectStreamFileStorage extends AbstractStorage<File> {
    private File directory;
    com.kuzmin.storage.serializer.IOStrategy IOStrategy;

    protected ObjectStreamFileStorage(File directory, IOStrategy IOStrategy) {
        Objects.requireNonNull(directory, "directory must not be null");
        if (!directory.isDirectory() || !directory.canWrite()) {
            throw new IllegalArgumentException(directory.toString() + " is not directory or is not writable");
        }
        this.directory = directory;
        this.IOStrategy = IOStrategy;
    }

    protected void updateResume(Resume resume, File file) {
        try {
            IOStrategy.doWrite(resume, new BufferedOutputStream(new FileOutputStream(file)));
        } catch (IOException e) {
            throw new StorageException("IO error", file.getAbsolutePath(), e);
        }
    }

    protected void saveResume(Resume resume, File file) {
        try {
            file.createNewFile();
            IOStrategy.doWrite(resume, new BufferedOutputStream(new FileOutputStream(file)));
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
            return IOStrategy.doRead(new BufferedInputStream(new FileInputStream(file)));
        } catch (IOException e) {
            throw new StorageException("IO error", file.getAbsolutePath(), e);
        }
    }

    protected List<Resume> getAll() {
        List<Resume> resumes = new ArrayList<>();
        File[] listFiles = directory.listFiles();
        if (listFiles != null) {
            for (File file : listFiles) {
                if (!file.isDirectory()) {
                    try {
                        resumes.add(IOStrategy.doRead(new BufferedInputStream(new FileInputStream(file))));
                    } catch (IOException e) {
                        throw new StorageException("IO error", file.getAbsolutePath(), e);
                    }
                }
            }
        }
        return resumes;
    }

    public void clear() {
        File[] listFiles = directory.listFiles();
        if (listFiles != null) {
            for (File file : listFiles) {
                if (!file.isDirectory()) {
                    file.delete();
                }
            }
        }
    }

    public int size() {
        int count = 0;
        File[] listFiles = directory.listFiles();
        if (listFiles != null) {
            for (File file : listFiles) {
                if (!file.isDirectory()) {
                    count++;
                }
            }
        }
        return count;
    }
}

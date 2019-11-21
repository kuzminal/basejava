package com.kuzmin.storage;

import com.kuzmin.exception.StorageException;
import com.kuzmin.model.Resume;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

public class ObjectStreamPathStorage extends AbstractStorage<Path>{
    private Path directory;
    WorkingStrategy workingStrategy;

    protected ObjectStreamPathStorage(Path directory) {
        Objects.requireNonNull(directory, "directory must not be null");
        if (!Files.isDirectory(directory) || !Files.isWritable(directory)) {
            throw new IllegalArgumentException(directory.toString() + " is not directory or is not writable");
        }
        this.directory = directory;
        this.workingStrategy = new ObjectStreamStrategy();
    }

    public void doWrite(Resume resume, Object os) throws IOException {
        workingStrategy.doWrite(resume,os);
    }

    public Resume doRead(Object is) throws IOException {
        return workingStrategy.doRead(is);
    }

    protected void updateResume(Resume resume, Path path) {
        try {
            doWrite(resume, Files.newOutputStream(path));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected void saveResume(Resume resume, Path path) {
        try {
            Files.createFile(path);
            doWrite(resume, Files.newOutputStream(path));
        } catch (IOException e) {
            throw new StorageException("IO error", path.toString(), e);
        }
    }

    protected void deleteResume(Path path) {
        try {
            Files.delete(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected Path getSearchKey(String path) {
        return Paths.get(directory.toString(), path);
    }

    protected boolean checkSearchKey(Path path) {
        return Files.exists(path);
    }

    protected Resume getResume(Path path) {
        try {
            return doRead(Files.newInputStream(path));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    protected List<Resume> getAll() {
        List<Resume> resumes = new ArrayList<>();
        Stream<Path> fileList = null;
        try {
            fileList = Files.list(directory);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (fileList != null) {
            fileList.filter(Files::isRegularFile)
                    .forEach(p -> {
                        try {
                            resumes.add(doRead(Files.newInputStream(p)));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
        }
        return resumes;
    }

    public void clear() {
        Stream<Path> fileList = null;
        try {
            fileList = Files.list(directory);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (fileList != null) {
            fileList.filter(Files::isRegularFile)
                    .forEach(p -> {
                                try {
                                    Files.delete(p);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                    );
        }
    }

    public int size() {
        long count = 0;
        Stream<Path> fileList = null;
        try {
            fileList = Files.list(directory);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (fileList != null) {
            count = fileList.filter(Files::isRegularFile).count();
        }
        return (int) count;
    }
}

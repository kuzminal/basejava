package com.kuzmin.storage;

import com.kuzmin.exception.StorageException;
import com.kuzmin.model.Resume;
import com.kuzmin.storage.serializer.IOStrategy;

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
    com.kuzmin.storage.serializer.IOStrategy IOStrategy;

    public ObjectStreamPathStorage(Path directory, IOStrategy IOStrategy) {
        Objects.requireNonNull(directory, "directory must not be null");
        if (!Files.isDirectory(directory) || !Files.isWritable(directory)) {
            throw new IllegalArgumentException(directory.toString() + " is not directory or is not writable");
        }
        this.directory = directory;
        this.IOStrategy = IOStrategy;
    }

    protected void updateResume(Resume resume, Path path) {
        try {
            IOStrategy.doWrite(resume, Files.newOutputStream(path));
        } catch (IOException e) {
            throw new StorageException("IO error", path.toString(), e);
        }
    }

    protected void saveResume(Resume resume, Path path) {
        try {
            Files.createFile(path);
            IOStrategy.doWrite(resume, Files.newOutputStream(path));
        } catch (IOException e) {
            throw new StorageException("IO error", path.toString(), e);
        }
    }

    protected void deleteResume(Path path) {
        try {
            Files.delete(path);
        } catch (IOException e) {
            throw new StorageException("IO error", path.toString(), e);
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
            return IOStrategy.doRead(Files.newInputStream(path));
        } catch (IOException e) {
            throw new StorageException("IO error", path.toString(), e);
        }
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
                            resumes.add(IOStrategy.doRead(Files.newInputStream(p)));
                        } catch (IOException e) {
                            throw new StorageException("IO error", directory.toString(), e);
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
            throw new StorageException("IO error", directory.toString(), e);
        }
        if (fileList != null) {
            fileList.filter(Files::isRegularFile)
                    .forEach(this::deleteResume);
        }
    }

    public int size() {
        long count = 0;
        Stream<Path> fileList = null;
        try {
            fileList = Files.list(directory);
        } catch (IOException e) {
            throw new StorageException("IO error", directory.toString(), e);
        }
        if (fileList != null) {
            count = fileList.filter(Files::isRegularFile).count();
        }
        return (int) count;
    }
}

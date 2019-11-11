package com.kuzmin.storage;

import com.kuzmin.exception.StorageException;
import com.kuzmin.model.Resume;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

public abstract class AbstractFileStorage extends AbstractStorage<File> {
    private File directory;

    protected AbstractFileStorage(File directory) {
        Objects.requireNonNull(directory, "directory must not be null");
        if (!directory.isDirectory()){
            throw new IllegalArgumentException(directory.getAbsolutePath() + " is not directory");
        }
        if (!directory.canRead() && directory.canWrite()){
            throw new IllegalArgumentException(directory.getAbsolutePath() + " is not readeble/writeble");
        }
        this.directory = directory;
    }

    @Override
    protected void updateResume(Resume resume, File file) {

    }

    @Override
    protected void saveResume(Resume resume, File file) {
        try {
            file.createNewFile();
            doWrite(resume, file);
        } catch (IOException e) {
            throw new StorageException("IO error", file.getName(), e);
        }
    }

    protected abstract void doWrite(Resume resume, File file) throws IOException;

    @Override
    protected void deleteResume(File file) {

    }

    @Override
    protected File getSearchKey(String file) {
        return new File(directory, file);
    }

    @Override
    protected boolean checkSearchKey(File file) {
        return file.exists();
    }

    @Override
    protected Resume getResume(File file) {
        return null;
    }

    @Override
    protected List<Resume> getAll() {
        return null;
    }

    @Override
    public void clear() {

    }

    @Override
    public int size() {
        return 0;
    }
}

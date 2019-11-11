package com.kuzmin.storage;

import com.kuzmin.model.Resume;

import java.io.File;
import java.util.List;

public class AbstractFileStorage extends AbstractStorage<File> {
    @Override
    protected void updateResume(Resume resume, File searchKey) {

    }

    @Override
    protected void saveResume(Resume resume, File searchKey) {

    }

    @Override
    protected void deleteResume(File searchKey) {

    }

    @Override
    protected File getSearchKey(String searchKey) {
        return null;
    }

    @Override
    protected boolean checkSearchKey(File searchKey) {
        return false;
    }

    @Override
    protected Resume getResume(File searchKey) {
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

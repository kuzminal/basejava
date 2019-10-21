package com.kuzmin.storage;

import com.kuzmin.model.Resume;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ListStorage extends AbstractStorage {
    private List<Resume> listStorage = new ArrayList<>();

    @Override
    public void clear() {
        listStorage.clear();
    }

    @Override
    protected Integer getSearchKey(Object resume) {
        for (int i = 0; i < listStorage.size(); i++) {
            if (listStorage.get(i).getUuid().equals(((Resume) resume).getUuid())) {
                return i;
            }
        }
        return null;
    }

    @Override
    protected Resume getResume(Object searchKey) {
        return listStorage.get((Integer) searchKey);
    }

    @Override
    protected List<Resume> getAll() {
        return listStorage;
    }

    @Override
    protected List<Resume> sortResumes(List<Resume> resumes) {
        return resumes.stream()
                .sorted()
                .collect(Collectors.toList());
    }

    @Override
    protected void updateResume(Resume resume, Object searchKey) {
        listStorage.set((Integer) searchKey, resume);
    }

    @Override
    protected void saveResume(Resume resume, Object searchKey) {
        listStorage.add(resume);
    }

    @Override
    protected void deleteResume(Object searchKey) {
        listStorage.remove((int) searchKey);
    }

    @Override
    public int size() {
        return listStorage.size();
    }

    public boolean checkSearchKey(Object searchKey) {
        return searchKey != null;
    }
}

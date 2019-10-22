package com.kuzmin.storage;

import com.kuzmin.model.Resume;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ResumeMapStorage extends AbstractStorage {
    private Map<String, Resume> resumes = new HashMap<>();

    @Override
    protected void updateResume(Resume resume, Object searchKey) {
        resumes.put(resume.getUuid(), resume);
    }

    @Override
    protected void saveResume(Resume resume, Object searchKey) {
        resumes.put(resume.getUuid(), resume);
    }

    @Override
    protected void deleteResume(Object searchKey) {
        resumes.remove(((Resume) searchKey).getUuid());
    }

    @Override
    protected Resume getSearchKey(Object resume) {
        return resumes.get(((Resume) resume).getUuid());
    }

    @Override
    protected boolean checkSearchKey(Object searchKey) {
        return searchKey != null;
    }

    @Override
    protected Resume getResume(Object searchKey) {
        return (Resume) searchKey;
    }

    @Override
    protected List<Resume> getAll() {
        return new ArrayList<>(resumes.values());
    }

    @Override
    public void clear() {
        resumes.clear();
    }

    @Override
    public int size() {
        return resumes.size();
    }
}

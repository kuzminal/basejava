package com.kuzmin.storage;

import com.kuzmin.model.Resume;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ResumeMapStorage extends AbstractStorage<Resume> {
    private Map<String, Resume> resumes = new HashMap<>();

    @Override
    protected void updateResume(Resume resume, Resume searchKey) {
        resumes.put(resume.getUuid(), resume);
    }

    @Override
    protected void saveResume(Resume resume, Resume searchKey) {
        resumes.put(resume.getUuid(), resume);
    }

    @Override
    protected void deleteResume(Resume searchKey) {
        resumes.remove((searchKey).getUuid());
    }

    @Override
    protected Resume getSearchKey(String uuid) {
        return resumes.get(uuid);
    }

    @Override
    protected boolean checkSearchKey(Resume searchKey) {
        return searchKey != null;
    }

    @Override
    protected Resume getResume(Resume searchKey) {
        return searchKey;
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

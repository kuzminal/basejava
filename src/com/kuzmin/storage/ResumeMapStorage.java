package com.kuzmin.storage;

import com.kuzmin.model.Resume;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
        resumes.remove(searchKey);
    }

    @Override
    protected Object getSearchKey(Object resume) {
        return ((Resume) resume).getUuid();
    }

    @Override
    protected boolean checkSearchKey(Object searchKey) {
        return resumes.containsKey(searchKey);
    }

    @Override
    protected Resume getResume(Object searchKey) {
        return resumes.get(searchKey);
    }

    @Override
    public void clear() {
        resumes.clear();
    }

    @Override
    public List<Resume> getSortedStorage() {
        return resumes.values().stream()
                .sorted(Resume::compareTo)
                .collect(Collectors.toList());
    }

    @Override
    public int size() {
        return resumes.size();
    }
}

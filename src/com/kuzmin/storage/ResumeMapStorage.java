package com.kuzmin.storage;

import com.kuzmin.model.Resume;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ResumeMapStorage extends AbstractStorage {
    private Map<String, Resume> resumes = new HashMap<>();

    @Override
    protected void updateObject(Resume r, Object key) {
        resumes.put(r.getUuid(), r);
    }

    @Override
    protected void saveObject(Resume r, Object key) {
        resumes.put(r.getUuid(), r);
    }

    @Override
    protected void deleteObject(Object key) {
        resumes.remove((Resume) key);
    }

    @Override
    protected Object getKey(Object resume) {
        return ((Resume) resume).getUuid();
    }

    @Override
    protected boolean checkKey(Object key) {
        return resumes.containsKey(((Resume) key).getUuid());
    }

    @Override
    protected Resume getResume(Object key) {
        return resumes.get(((Resume) key).getUuid());
    }

    @Override
    public void clear() {
        resumes.clear();
    }

    @Override
    public List<Resume> getAllSorted() {
        return resumes.values().stream()
                .sorted(Resume::compareTo)
                .collect(Collectors.toList());
    }

    @Override
    public int size() {
        return resumes.size();
    }
}

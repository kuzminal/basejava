package com.kuzmin.storage;

import com.kuzmin.model.Resume;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class HashMapStorage extends AbstractStorage {
    private Map<Integer, Resume> resumes = new HashMap<>();

    @Override
    protected void updateObject(Resume r, Object key) {
        resumes.put(r.hashCode(), r);
    }

    @Override
    protected void saveObject(Resume r, Object key) {
        resumes.put(r.hashCode(), r);
    }

    @Override
    protected void deleteObject(Object key) {
        resumes.remove(key.hashCode());
    }

    @Override
    protected Object getKey(String uuid) {
        return uuid;
    }

    @Override
    protected boolean checkKey(Object key) {
        return resumes.containsKey(key.hashCode());
    }

    @Override
    protected Resume getResume(Object key) {
        return resumes.get(key.hashCode());
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

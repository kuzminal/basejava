package com.kuzmin.storage;

import com.kuzmin.model.Resume;

import java.util.Map;
import java.util.TreeMap;

public class MapStorage extends AbstractStorage {
    private Map<String, Resume> resumes = new TreeMap<>();

    @Override
    protected void updateObject(Resume r, Object key) {
        resumes.put((String) key, r);// put  и для сохранения и для обновления т.к. если ключ уже есть то значение перезапишется
    }

    @Override
    protected void saveObject(Resume r, Object key) {
        resumes.put((String) key, r);
    }

    @Override
    protected void deleteObject(Object key) {
        resumes.remove(key);
    }

    @Override
    protected Object getKey(String uuid) {
        return uuid; // возвращаю uuid потому что для мапы не нужен поиск индекса
    }

    @Override
    protected boolean checkKey(Object key) {
        return resumes.containsKey(key);
    }

    @Override
    protected Resume getResume(Object key) {
        return resumes.get(key);
    }

    @Override
    public void clear() {
        resumes.clear();
    }

    @Override
    public Resume[] getAll() {
        return resumes.values().toArray(new Resume[resumes.size()]);
    }

    @Override
    public int size() {
        return resumes.size();
    }
}

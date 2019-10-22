package com.kuzmin.storage;

import com.kuzmin.model.Resume;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MapStorage extends AbstractStorage {
    private Map<String, Resume> resumes = new HashMap<>();

    @Override
    protected void updateResume(Resume resume, Object searchKey) {
        resumes.put((String) searchKey, resume);// put  и для сохранения и для обновления т.к. если ключ уже есть то значение перезапишется
    }

    @Override
    protected void saveResume(Resume resume, Object searchKey) {
        resumes.put((String) searchKey, resume);
    }

    @Override
    protected void deleteResume(Object searchKey) {
        resumes.remove(searchKey);
    }

    @Override
    protected String getSearchKey(Object resume) {
        return ((Resume) resume).getUuid(); // возвращаю uuid потому что для мапы не нужен поиск индекса
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

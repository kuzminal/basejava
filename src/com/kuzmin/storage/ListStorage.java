package com.kuzmin.storage;

import com.kuzmin.model.Resume;

import java.util.ArrayList;
import java.util.List;

public class ListStorage extends AbstractStorage<Integer> {
    private List<Resume> listStorage = new ArrayList<>();

    @Override
    public void clear() {
        listStorage.clear();
    }

    @Override
    protected Integer getSearchKey(String uuid) {
        for (int i = 0; i < listStorage.size(); i++) {
            if (listStorage.get(i).getUuid().equals(uuid)) {
                return i;
            }
        }
        return null;
    }

    @Override
    protected Resume getResume(Integer searchKey) {
        return listStorage.get(searchKey);
    }

    @Override
    protected List<Resume> getAll() {
        return listStorage;
    }

    @Override
    protected void updateResume(Resume resume, Integer searchKey) {
        listStorage.set(searchKey, resume);
    }

    @Override
    protected void saveResume(Resume resume, Integer searchKey) {
        listStorage.add(resume);
    }

    @Override
    protected void deleteResume(Integer searchKey) {
        listStorage.remove((int) searchKey);
    }

    @Override
    public int size() {
        return listStorage.size();
    }

    public boolean checkSearchKey(Integer searchKey) {
        return searchKey != null;
    }
}

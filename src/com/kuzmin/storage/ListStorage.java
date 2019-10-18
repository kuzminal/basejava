package com.kuzmin.storage;

import com.kuzmin.model.Resume;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ListStorage extends AbstractStorage {
    private List<Resume> listStorage = new ArrayList<>();

    @Override
    public void clear() {
        listStorage.clear();
    }

    @Override
    protected Integer getKey(Object resume) {
        for (int i = 0; i < listStorage.size(); i++) {
            if (listStorage.get(i).getUuid().equals(((Resume) resume).getUuid())) {
                return i;
            }
        }
        return null;
    }

    @Override
    protected Resume getResume(Object key) {
        return listStorage.get((Integer) key);
    }

    @Override
    protected void updateObject(Resume r, Object key) {
        listStorage.set((Integer) key, r);
    }

    @Override
    protected void saveObject(Resume r, Object key) {
        listStorage.add(r);
    }

    @Override
    protected void deleteObject(Object key) {
        listStorage.remove((int) key);
    }

    @Override
    public List<Resume> getAllSorted() {
        listStorage.sort(Comparator.naturalOrder());
        return listStorage;
    }

    @Override
    public int size() {
        return listStorage.size();
    }

    public boolean checkKey(Object key) {
        return key != null;
    }
}

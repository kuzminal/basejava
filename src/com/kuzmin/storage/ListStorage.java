package com.kuzmin.storage;

import com.kuzmin.model.Resume;

import java.util.ArrayList;
import java.util.List;

public class ListStorage extends AbstractStorage {
    private List<Resume> listStorage = new ArrayList<>();

    @Override
    public void clear() {
        listStorage.clear();
    }

    @Override
    protected Resume getElement(String uuid) {
        for (int i=0; i < listStorage.size(); i++){
            if (listStorage.get(i).getUuid().equals(uuid)){
                return listStorage.get(i);
            }
        }
        return null;
    }

    @Override
    protected Resume getResume(Object key) {
        for (Resume resume : listStorage){
            if (resume.equals(key)){
                return resume;
            }
        }
        return null;
    }

    @Override
    protected boolean contains(Object key) {
        for (Resume resume : listStorage) {
            if (resume.equals(key)) {
                return true;
            }
        }
        return false;
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
            listStorage.remove(key);
    }

    @Override
    public Resume[] getAll() {
        return listStorage.toArray(new Resume[listStorage.size()]);
    }

    @Override
    public int size() {
        return listStorage.size();
    }
}

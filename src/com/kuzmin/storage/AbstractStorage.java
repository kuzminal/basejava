package com.kuzmin.storage;

import com.kuzmin.exception.ExistStorageException;
import com.kuzmin.exception.NotExistStorageException;
import com.kuzmin.model.Resume;

public abstract class AbstractStorage implements Storage {

    protected abstract Resume getResume(Object key);

    protected abstract boolean contains(Object key);

    protected abstract void updateObject(Resume r, Object key);

    protected abstract void saveObject(Resume r, Object key);

    protected abstract void deleteObject(Object searchKey);

    protected abstract Object getElement(String uuid);

    public Resume get(String uuid) {
        return getResume(getExistedElement(uuid));
    }

    public void update(Resume r) {
        updateObject(r, getExistedElement(r.getUuid()));
    }

    public void save(Resume r) {
        saveObject(r, getNotExistedSearchKey(r.getUuid()));
    }

    public void delete(String uuid) {
        deleteObject(getExistedElement(uuid));
    }

    private Object getExistedElement(String uuid) {
        Object key = getElement(uuid);
        if (!contains(key)) {
            throw new NotExistStorageException(uuid);
        }
        return key;
    }

    private Object getNotExistedSearchKey(String uuid) {
        Object key = getElement(uuid);
        if (contains(key)) {
            throw new ExistStorageException(uuid);
        }
        return key;
    }
}

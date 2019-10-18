package com.kuzmin.storage;

import com.kuzmin.exception.ExistStorageException;
import com.kuzmin.exception.NotExistStorageException;
import com.kuzmin.model.Resume;

public abstract class AbstractStorage implements Storage {

    protected abstract void updateObject(Resume r, Object key);

    protected abstract void saveObject(Resume r, Object key);

    protected abstract void deleteObject(Object key);

    protected abstract Object getKey(Object uuid);

    protected abstract boolean checkKey(Object key);

    protected abstract Resume getResume(Object key);

    public Resume get(Resume resume) {
        return getResume(getExistedElement(resume));
    }

    public void update(Resume r) {
        updateObject(r, getExistedElement(r));
    }

    public void save(Resume r) {
        saveObject(r, getNotExistedSearchKey(r));
    }

    public void delete(Resume resume) {
        deleteObject(getExistedElement(resume));
    }

    private Object getExistedElement(Resume resume) {
        Object key = getKey(resume);
        if (!checkKey(key)) {
            throw new NotExistStorageException(resume.getUuid());
        }
        return key;
    }

    private Object getNotExistedSearchKey(Resume resume) {
        Object key = getKey(resume);
        if (checkKey(key)) {
            throw new ExistStorageException(resume.getUuid());
        }
        return key;
    }
}

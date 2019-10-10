package com.kuzmin.storage;

import com.kuzmin.exception.StorageException;
import com.kuzmin.model.Resume;

import java.util.Arrays;

public abstract class AbstractArrayStorage extends AbstractStorage {
    protected static final int STORAGE_LIMIT = 10_000;

    protected Resume[] storage = new Resume[STORAGE_LIMIT];

    protected int size = 0;

    public abstract void insert(Resume resume, Object index);

    public abstract void fillEmptySpace(int index);

    protected abstract Integer getKey(String uuid);

    public int size() {
        return size;
    }

    public void clear() {
        Arrays.fill(storage, 0, size, null);
        size = 0;
    }

    public boolean checkKey(Object key) {
        return (Integer) key >= 0;
    }

    @Override
    public void updateObject(Resume resume, Object index) {
        storage[(int) index] = resume;
    }

    @Override
    public void saveObject(Resume resume, Object key) {
        if (size == STORAGE_LIMIT) {
            throw new StorageException("Storage is full", resume.getUuid());
        } else {
            insert(resume, key);
            size++;
        }
    }

    @Override
    public void deleteObject(Object index) {
        fillEmptySpace((Integer) index);
        storage[size - 1] = null;
        size--;
    }

    public Resume[] getAll() {
        return Arrays.copyOf(storage, size);
    }

    public Resume getResume(Object index) {
        return storage[(Integer) index];
    }
}

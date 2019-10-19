package com.kuzmin.storage;

import com.kuzmin.model.Resume;

/**
 * Array based storage for Resumes
 */
public class ArrayStorage extends AbstractArrayStorage {

    @Override
    public void insert(Resume resume, Object index) {
        storage[size] = resume;
    }

    @Override
    public void fillEmptySpace(int index) {
        storage[index] = storage[size - 1];
    }

    @Override
    protected Integer getSearchKey(Object resume) {
        for (int i = 0; i < size; i++) {
            if (storage[i].getUuid().equals(((Resume) resume).getUuid())) {
                return i;
            }
        }
        return -1;
    }
}

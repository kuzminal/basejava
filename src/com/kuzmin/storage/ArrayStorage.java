package com.kuzmin.storage;

import com.kuzmin.model.Resume;

/**
 * Array based storage for Resumes
 */
public class ArrayStorage extends AbstractArrayStorage{

    @Override
    public void insertNewResume(Resume resume) {
        storage[size] = resume;
        size++;
    }

    @Override
    public void fillEmptySpace(int index) {
        storage[index] = storage[size - 1];
    }

    protected int getIndex(String uuid) {
        for (int i = 0; i < size; i++) {
            if (storage[i].getUuid().equals(uuid)) {
                return i;
            }
        }
        return -1;
    }
}

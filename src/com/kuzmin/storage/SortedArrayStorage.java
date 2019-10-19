package com.kuzmin.storage;

import com.kuzmin.model.Resume;

import java.util.Arrays;

public class SortedArrayStorage extends AbstractArrayStorage {
    @Override
    protected Integer getSearchKey(Object uuid) {
        return Arrays.binarySearch(storage, 0, size, (Resume) uuid);
    }

    @Override
    public void fillEmptySpace(int index) {
        int count = size - index - 1;
        System.arraycopy(storage, index + 1, storage, index, count);
    }

    @Override
    public void insert(Resume resume, Object index) {
        int key = Math.abs((Integer) index) - 1;
        int count = size - key;
        if (count > 0) {
            System.arraycopy(storage, key, storage, key + 1, count);
        }
        storage[key] = resume;
    }
}

package com.kuzmin.storage;

import com.kuzmin.model.Resume;

import java.util.Arrays;

public class SortedArrayStorage extends AbstractArrayStorage {
    @Override
    protected int getIndex(String uuid) {
        Resume searchKey = new Resume();
        searchKey.setUuid(uuid);
        return Arrays.binarySearch(storage, 0, size, searchKey);
    }

    @Override
    public void fillEmptySpace(int index) {
        int count = size - index - 1;
        System.arraycopy(storage, index + 1, storage, index, count);
    }

    @Override
    public void insert(Resume resume) {
        int index = Math.abs(Arrays.binarySearch(storage, 0, size, resume)) - 1;
        int count = size - index;
        if (count > 0) {
            System.arraycopy(storage, index, storage, index + 1, count);
        }
        storage[index] = resume;
    }
}

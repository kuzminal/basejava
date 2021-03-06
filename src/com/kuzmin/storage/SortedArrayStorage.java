package com.kuzmin.storage;

import com.kuzmin.model.Resume;

import java.util.Arrays;
import java.util.Comparator;

public class SortedArrayStorage extends AbstractArrayStorage {

    @Override
    protected Integer getSearchKey(String uuid) {
        Resume searchKey = new Resume((String) uuid, "Ivanov");
        Comparator<Resume> uuidComparator = Comparator.comparing(Resume::getUuid);
        return Arrays.binarySearch(storage, 0, size, searchKey, uuidComparator);
    }

    @Override
    public void fillEmptySpace(int index) {
        int count = size - index - 1;
        System.arraycopy(storage, index + 1, storage, index, count);
    }

    @Override
    public void insert(Resume resume, Integer index) {
        int key = Math.abs((Integer) index) - 1;
        int count = size - key;
        if (count > 0) {
            System.arraycopy(storage, key, storage, key + 1, count);
        }
        storage[key] = resume;
    }
}

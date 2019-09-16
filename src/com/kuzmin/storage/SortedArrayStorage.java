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
        for (int j = index; j < size - 1; j++) {
            storage[j] = storage[j + 1];
            storage[j + 1] = null;
        }
    }

    @Override
    public void insertNewResume(Resume resume) {
        int index = 0;
        for (int i=0; i < size; i++){
            if (storage[i].compareTo(resume) >= 0){
                index = i;
            }else index = i + 1;
        }
        if (size == 0){
            storage[0] = resume;
        } else if (size == index)
        {
            storage[index] = resume;
        }else {
            for (int j = size-1; j >= index; j--) {
                storage[j + 1] = storage[j];
                storage[j] = resume;
            }
        }
        size++;
    }
}

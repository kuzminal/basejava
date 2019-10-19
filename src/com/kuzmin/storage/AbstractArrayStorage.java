package com.kuzmin.storage;

import com.kuzmin.exception.StorageException;
import com.kuzmin.model.Resume;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public abstract class AbstractArrayStorage extends AbstractStorage {
    protected static final int STORAGE_LIMIT = 10_000;

    protected Resume[] storage = new Resume[STORAGE_LIMIT];

    protected int size = 0;

    public abstract void insert(Resume resume, Object index);

    public abstract void fillEmptySpace(int index);

    protected abstract Integer getSearchKey(Object uuid);

    public int size() {
        return size;
    }

    public void clear() {
        Arrays.fill(storage, 0, size, null);
        size = 0;
    }

    public boolean checkSearchKey(Object searchKey) {
        return (Integer) searchKey >= 0;
    }

    @Override
    public void updateResume(Resume resume, Object index) {
        storage[(int) index] = resume;
    }

    @Override
    public void saveResume(Resume resume, Object searchKey) {
        if (size == STORAGE_LIMIT) {
            throw new StorageException("Storage is full", resume.getUuid());
        } else {
            insert(resume, searchKey);
            size++;
        }
    }

    @Override
    public void deleteResume(Object index) {
        fillEmptySpace((Integer) index);
        storage[size - 1] = null;
        size--;
    }

    public List<Resume> getSortedStorage() {
        List<Resume> resumes = Arrays.asList(Arrays.copyOf(storage, size));
        resumes.sort(Comparator.naturalOrder());
        return resumes;
    }

    public Resume getResume(Object index) {
        return storage[(Integer) index];
    }
}

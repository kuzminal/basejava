package com.kuzmin.storage;

import com.kuzmin.exception.StorageException;
import com.kuzmin.model.Resume;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public abstract class AbstractArrayStorage extends AbstractStorage<Integer> {
    protected static final int STORAGE_LIMIT = 10_000;

    protected Resume[] storage = new Resume[STORAGE_LIMIT];

    protected int size = 0;

    public abstract void insert(Resume resume, Integer index);

    public abstract void fillEmptySpace(int index);

    public int size() {
        return size;
    }

    public void clear() {
        Arrays.fill(storage, 0, size, null);
        size = 0;
    }

    public boolean checkSearchKey(Integer index) {
        return index >= 0;
    }

    @Override
    public void updateResume(Resume resume, Integer index) {
        storage[index] = resume;
    }

    @Override
    public void saveResume(Resume resume, Integer searchKey) {
        if (size == STORAGE_LIMIT) {
            throw new StorageException("Storage is full", resume.getUuid());
        } else {
            insert(resume, searchKey);
            size++;
        }
    }

    @Override
    public void deleteResume(Integer index) {
        fillEmptySpace(index);
        storage[size - 1] = null;
        size--;
    }

    @Override
    public List<Resume> getAll() {
        return Arrays.asList(Arrays.copyOf(storage, size));
    }

    public List<Resume> sortResumes(List<Resume> resumes) {
        resumes.sort(Comparator.naturalOrder());
        return resumes;
    }

    public Resume getResume(Integer index) {
        return storage[index];
    }
}

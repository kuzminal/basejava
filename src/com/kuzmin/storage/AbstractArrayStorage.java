package com.kuzmin.storage;

import com.kuzmin.model.Resume;

import java.util.Arrays;

public abstract class AbstractArrayStorage implements Storage{
    protected static final int STORAGE_LIMIT = 10000;

    protected Resume[] storage = new Resume[STORAGE_LIMIT];
    protected int size = 0;

    public int size() {
        return size;
    }

    public void clear() {
        Arrays.fill(storage, 0, size, null);
        size = 0;
    }

    public Resume get(String uuid) {
        int index = getIndex(uuid);
        if (index == -1) {
            System.out.println("Resume " + uuid + " not exist");
            return null;
        }
        return storage[index];
    }

    public void update(Resume resume) {
        int index = getIndex(resume.getUuid());
        if (index != -1) {
            storage[index] = resume;
            System.out.println("Resume " + resume.getUuid() + " was updated");
        } else {
            System.out.println("Resume " + resume.getUuid() + " is not found in storage. Nothing to update");
        }
    }

    public void save(Resume resume) {
        int index = getIndex(resume.getUuid());
        if (index >= 0) {
            System.out.println("Resume " + resume.getUuid() + " is already exist");
        } else {
            if (size == STORAGE_LIMIT) {
                System.out.println("Storage is full");
            } else {
                insertNewResume(resume);
            }
        }
    }

    public abstract void fillEmptySpace(int index);

    public void delete(String uuid) {
        int index = getIndex(uuid);
        if (index != -1) {
            fillEmptySpace(index);
            storage[size - 1] = null;
            size--;
        } else {
            System.out.println("Resume " + uuid + " is not found in storage");
        }
    }

    public abstract void insertNewResume(Resume resume);

    public Resume[] getAll() {
        return Arrays.copyOfRange(storage, 0, size);
    }

    protected abstract int getIndex(String uuid);
}

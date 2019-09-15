package com.kuzmin.storage;

import com.kuzmin.model.Resume;

import java.util.Arrays;

/**
 * Array based storage for Resumes
 */
public class ArrayStorage {
    private Resume[] storage = new Resume[10_000];
    private int size = 0;

    public void clear() {
        Arrays.fill(storage, 0, size, null);
        size = 0;
    }

    public void save(Resume resume) {
        int index = getIndex(resume.toString());
        if (index != -1) {
            System.out.println("Resume " + resume.toString() + " is already exist");
        } else {
            if (size < storage.length) {
                storage[size] = resume;
                size++;
            } else {
                System.out.println("Storage is full");
            }
        }
    }

    public Resume get(String uuid) {
        int index = getIndex(uuid);
        if (index != -1) {
            return storage[index];
        }
        System.out.println("Resume " + uuid + " is not present in list");
        return null;
    }

    public void delete(String uuid) {
        int index = getIndex(uuid);
        if (index != -1) {
            storage[index] = storage[size - 1];
            storage[size - 1] = null;
            size--;
        } else {
            System.out.println("Resume " + uuid + " is not found in storage");
        }
    }

    /**
     * @return array, contains only Resumes in storage (without null)
     */
    public Resume[] getAll() {
        return Arrays.copyOfRange(storage, 0, size);
    }

    public int size() {
        return size;
    }

    /**
     * HW2
     */
    public void update(Resume resume) {
        int index = getIndex(resume.toString());
        if (index != -1) {
            storage[index] = resume;
            System.out.println("Resume " + resume.toString() + " was updated");
        } else {
            System.out.println("Resume " + resume.toString() + " is not found in storage. Nothing to update");
        }
    }

    private int getIndex(String uuid) {
        for (int i = 0; i < size; i++) {
            if (storage[i].toString().equals(uuid)) {
                return i;
            }
        }
        return -1;
    }
}

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

    public void save(Resume r) {
        int index = getElementIndex(r.uuid);
        if (index != -1){
            System.out.println("Resume " + r.uuid + " is already exist");
        }else {
            if (size < storage.length) {  // проверяю не превышен ли размер
                storage[size] = r;
                size++;
            }else {
                System.out.println("Storage is full");
            }
        }
    }

    public Resume get(String uuid) {
        int index = getElementIndex(uuid);
        if (index != -1){
            return storage[index];
        }
        System.out.println("Resume " + uuid + " is not present in list");
        return null;
    }

    public void delete(String uuid) {
        boolean hasFound = false; // не красиво конечно, но пока так.
        int index = getElementIndex(uuid);
        if (index != -1){
            storage[index] = storage[size - 1];
            storage[size - 1] = null;            // обнуляю последний элемент
            size--; // после смещение уменьшаю размер массива
        }else
        {
            System.out.println("Resume " + uuid + " is not found in storage");
        }
    }

    /**
     * @return array, contains only Resumes in storage (without null)
     */
    public Resume[] getAll() {
        return Arrays.copyOfRange(storage, 0, size); // возврат копии storage размером size
    }

    public int size() {
        return size;
    }

    /**
     * HW2
     */
    public void update(Resume resume){
        int index = getElementIndex(resume.uuid);
        if (index != -1){
            storage[index] = resume;
            System.out.println("Resume " + resume.uuid + " was updated");
        }else {
            System.out.println("Resume " + resume.uuid + " is not found in storage. Nothing to update");
        }
    }
    // добавил новы метод и везде использую теперь его
    public int getElementIndex(String uuid){
        for (int i = 0; i < size; i++) {
            if (storage[i].uuid.equals(uuid)){
                return i;
            }
        }
        return -1;
    }
}

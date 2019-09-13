package com.kuzmin.storage;

import com.kuzmin.model.Resume;

import java.util.Arrays;

/**
 * Array based storage for Resumes
 */
public class ArrayStorage {
    Resume[] storage = new Resume[10_000];
    private int size = 0;

    public void clear() {
        Arrays.fill(storage, 0, size, null); // добавил заполение null из Arrays
        size = 0;
    }

    public void save(Resume r) {
        if (get(r.uuid) == null) { //проверят нет ли уже записи с таким же UUID
            if (size < storage.length) {  // проверяю не превышен ли размер
                storage[size] = r;
                size++;
            }else {
                System.out.println("Storage is full");
            }
        }else{
            System.out.println("Resume " + r.uuid + " is already exist");
        }
    }

    public Resume get(String uuid) {
        for (int i = 0; i < size; i++) {
            if (storage[i].uuid.equals(uuid)) {
                return storage[i];
            }
        }
        System.out.println("Resume " + uuid + " is not present in list");
        return null;
    }

    public void delete(String uuid) {
        boolean hasFound = false; // не красиво конечно, но пока так.
        for (int i = 0; i < size; i++) {
            if (storage[i].uuid.equals(uuid)) {
                hasFound = true; // через булеву переменную проверяю найден элемент массива или нет
                /* убрал смещение и сделал к в лекции */
                storage[i] = storage[size - 1];
                storage[size - 1] = null;            // обнуляю последний элемент
                size--; // после смещение уменьшаю размер массива
            }
        }
        if (!hasFound){
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
        if (get(resume.uuid) == null){
            System.out.println("Resume " + resume.uuid + " is not found in storage. Nothing to update");
        }else {
            for (int i = 0; i < size; i++) {
                if (storage[i].uuid.equals(resume.uuid)) {
                    storage[i] = resume;
                    System.out.println("Resume " + resume.uuid + " was updated");
                }
            }
        }
    }
}

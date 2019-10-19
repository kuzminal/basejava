package com.kuzmin;

import com.kuzmin.model.Resume;
import com.kuzmin.storage.ArrayStorage;
import com.kuzmin.storage.SortedArrayStorage;

/**
 * Test for your com.kuzmin.storage.ArrayStorage implementation
 */
public class MainTestArrayStorage {
    public static final ArrayStorage ARRAY_STORAGE = new ArrayStorage();
    public static final SortedArrayStorage SORTED_ARRAY_STORAGE = new SortedArrayStorage();

    public static void main(String[] args) {
        Resume resume1 = new Resume("Ivanov");
        resume1.setUuid("uuid1");
        Resume resume2 = new Resume("Petrov");
        resume2.setUuid("uuid2");
        Resume resume3 = new Resume("Sidorov");
        resume3.setUuid("uuid3");
        Resume resume4 = new Resume("Abramovich");
        resume4.setUuid("uuid4");
        Resume resume5 = new Resume("Vasserman");
        resume5.setUuid("uuid5");

        ARRAY_STORAGE.save(resume1);
        ARRAY_STORAGE.save(resume2);
        ARRAY_STORAGE.save(resume3);

        SORTED_ARRAY_STORAGE.save(resume2);
        SORTED_ARRAY_STORAGE.save(resume1);
        SORTED_ARRAY_STORAGE.save(resume4);
        SORTED_ARRAY_STORAGE.save(resume3);
        SORTED_ARRAY_STORAGE.save(resume5);
        printAllSorted();

       // System.out.println("Get r1: " + ARRAY_STORAGE.get(r1.getUuid()));
        System.out.println("Size: " + ARRAY_STORAGE.size());

        //System.out.println("Get dummy: " + ARRAY_STORAGE.get("dummy"));

        printAll();
        //ARRAY_STORAGE.delete(r1.getUuid());
        printAll();
        //SORTED_ARRAY_STORAGE.delete(r3.getUuid());
        printAllSorted();

        ARRAY_STORAGE.update(resume3);
        ARRAY_STORAGE.clear();
        printAll();
        ARRAY_STORAGE.update(resume1);


        System.out.println("Size: " + ARRAY_STORAGE.size());
    }

    public static void printAll() {
        System.out.println("\nGet All");
        for (Resume resume : ARRAY_STORAGE.getAllSorted()) {
            System.out.println(resume);
        }
    }

    public static void printAllSorted() {
        System.out.println("\nGet All from sorted");
        for (Resume resume : SORTED_ARRAY_STORAGE.getAllSorted()) {
            System.out.println(resume);
        }
    }
}

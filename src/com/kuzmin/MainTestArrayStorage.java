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
        Resume r1 = new Resume();
        r1.setUuid("uuid1");
        Resume r2 = new Resume();
        r2.setUuid("uuid2");
        Resume r3 = new Resume();
        r3.setUuid("uuid3");
        Resume r4 = new Resume();
        r4.setUuid("uuid4");
        Resume r5 = new Resume();
        r5.setUuid("uuid5");

        ARRAY_STORAGE.save(r1);
        ARRAY_STORAGE.save(r2);
        ARRAY_STORAGE.save(r3);

        SORTED_ARRAY_STORAGE.save(r2);
        SORTED_ARRAY_STORAGE.save(r1);
        SORTED_ARRAY_STORAGE.save(r4);
        SORTED_ARRAY_STORAGE.save(r3);
        SORTED_ARRAY_STORAGE.save(r5);
        printAllSorted();

       // System.out.println("Get r1: " + ARRAY_STORAGE.get(r1.getUuid()));
        System.out.println("Size: " + ARRAY_STORAGE.size());

        //System.out.println("Get dummy: " + ARRAY_STORAGE.get("dummy"));

        printAll();
        //ARRAY_STORAGE.delete(r1.getUuid());
        printAll();
        //SORTED_ARRAY_STORAGE.delete(r3.getUuid());
        printAllSorted();

        ARRAY_STORAGE.update(r3);
        ARRAY_STORAGE.clear();
        printAll();
        ARRAY_STORAGE.update(r1);


        System.out.println("Size: " + ARRAY_STORAGE.size());
    }

    public static void printAll() {
        System.out.println("\nGet All");
        for (Resume r : ARRAY_STORAGE.getAllSorted()) {
            System.out.println(r);
        }
    }

    public static void printAllSorted() {
        System.out.println("\nGet All from sorted");
        for (Resume r : SORTED_ARRAY_STORAGE.getAllSorted()) {
            System.out.println(r);
        }
    }
}

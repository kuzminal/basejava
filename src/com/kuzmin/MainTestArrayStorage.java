package com.kuzmin;

import com.kuzmin.model.Resume;
import com.kuzmin.storage.ArrayStorage;

/**
 * Test for your com.kuzmin.storage.ArrayStorage implementation
 */
public class MainTestArrayStorage {
    public static final ArrayStorage ARRAY_STORAGE = new ArrayStorage();

    public static void main(String[] args) {
        Resume r1 = new Resume();
        r1.uuid = "uuid1";
        Resume r2 = new Resume();
        r2.uuid = "uuid2";
        Resume r3 = new Resume();
        r3.uuid = "uuid3";

        ARRAY_STORAGE.save(r1);
        ARRAY_STORAGE.save(r2);
        ARRAY_STORAGE.save(r3);

        System.out.println("Get r1: " + ARRAY_STORAGE.get(r1.uuid));
        System.out.println("Size: " + ARRAY_STORAGE.size());

        System.out.println("Get dummy: " + ARRAY_STORAGE.get("dummy"));

        printAll();
        ARRAY_STORAGE.delete(r1.uuid);
        printAll();
        ARRAY_STORAGE.update(r3);
        ARRAY_STORAGE.clear();
        printAll();
        ARRAY_STORAGE.update(r1);


        System.out.println("Size: " + ARRAY_STORAGE.size());
    }

    public static void printAll() {
        System.out.println("\nGet All");
        for (Resume r : ARRAY_STORAGE.getAll()) {
            System.out.println(r);
        }
    }
}

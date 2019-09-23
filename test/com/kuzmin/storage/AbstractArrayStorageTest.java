package com.kuzmin.storage;

import com.kuzmin.model.Resume;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


class AbstractArrayStorageTest {
    private static Storage storage;
    private static final String UUID1 = "uuid1";
    private static final String UUID2 = "uuid2";
    private static final String UUID3 = "uuid3";

    public AbstractArrayStorageTest(Storage storage){
        this.storage = storage;
        this.storage.save(new Resume(UUID1));
        this.storage.save(new Resume(UUID2));
        this.storage.save(new Resume(UUID3));
    }

    @Test
    void size() {
        Assertions.assertEquals(3, storage.size());
    }

    @Test
    void clear() {
        storage.clear();
        Assertions.assertEquals(0, storage.size());
    }

    @Test
    void get() {
        Resume resume1 = new Resume(UUID1);
        Resume resume2 = new Resume(UUID2);
        Resume resume3 = new Resume(UUID3);
        Assertions.assertEquals(resume1, storage.get(UUID1));
        Assertions.assertEquals(resume2, storage.get(UUID2));
        Assertions.assertEquals(resume3, storage.get(UUID3));
    }

    @Test
    void update() {
        Resume resume = new Resume(UUID2);
        Assertions.assertEquals(resume, storage.get(UUID2));
    }

    @Test
    void save() {
        Resume resume = new Resume(UUID3);
        Assertions.assertEquals(resume, storage.get(UUID3));
    }

    @Test
    void delete() {
        storage.delete(UUID3);
        Assertions.assertEquals(2, storage.size());
    }

    @Test
    void getAll() {
        Resume[] list = storage.getAll();
        Assertions.assertEquals(3, list.length);
        Assertions.assertEquals(new Resume(UUID1), list[0]);
        Assertions.assertEquals(new Resume(UUID2), list[1]);
        Assertions.assertEquals(new Resume(UUID3), list[2]);
    }
}
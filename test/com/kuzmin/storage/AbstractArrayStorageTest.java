package com.kuzmin.storage;

import com.kuzmin.exception.ExistStorageException;
import com.kuzmin.exception.NotExistStorageException;
import com.kuzmin.exception.StorageException;
import com.kuzmin.model.Resume;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

abstract class AbstractArrayStorageTest {
    private Storage storage;
    private static final String UUID1 = "uuid1";
    private static final String UUID2 = "uuid2";
    private static final String UUID3 = "uuid3";
    private static final String UUID4 = "uuid4";
    private static final Resume RESUME1 = new Resume(UUID1);
    private static final Resume RESUME2 = new Resume(UUID2);
    private static final Resume RESUME3 = new Resume(UUID3);
    private static final Resume RESUME4 = new Resume(UUID4);

    public AbstractArrayStorageTest(Storage storage){
        this.storage = storage;
    }

    @BeforeEach
    public void setUp(){
        storage.clear();
        storage.save(RESUME1);
        storage.save(RESUME2);
        storage.save(RESUME3);
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
        Assertions.assertEquals(RESUME1, storage.get(UUID1));
        Assertions.assertEquals(RESUME2, storage.get(UUID2));
        Assertions.assertEquals(RESUME3, storage.get(UUID3));
    }

    @Test
    void getNotExist(){
        Assertions.assertThrows(NotExistStorageException.class, () -> {storage.get(UUID4);});
    }

    @Test
    void update() {
        Assertions.assertEquals(RESUME2, storage.get(UUID2));
    }

    @Test
    void updateNotExist(){
        Assertions.assertThrows(NotExistStorageException.class, () -> {storage.update(RESUME4);});
    }

    @Test
    void save() {
        storage.save(RESUME4);
        Assertions.assertEquals(RESUME4, storage.get(UUID4));
        Assertions.assertEquals(4, storage.size());
    }

    @Test
    void saveExist(){
        Assertions.assertThrows(ExistStorageException.class,() -> {storage.save(RESUME1);});
    }

    @Test
    void saveOverLimit(){
        Assertions.assertThrows(StorageException.class,() -> {
            for (int i = 3; i <= AbstractArrayStorage.STORAGE_LIMIT; i++) {
                storage.save(new Resume());
            }
        });
    }

    @Test
    void delete() {
        storage.delete(UUID3);
        Assertions.assertEquals(2, storage.size());
    }

    @Test
    void deleteNotExist(){
        Assertions.assertThrows(NotExistStorageException.class,() -> {storage.get(UUID4);});
    }

    @Test
    void getAll() {
        Resume[] list = storage.getAll();
        Assertions.assertEquals(3, list.length);
        Assertions.assertEquals(RESUME1, list[0]);
        Assertions.assertEquals(RESUME2, list[1]);
        Assertions.assertEquals(RESUME3, list[2]);
    }
}
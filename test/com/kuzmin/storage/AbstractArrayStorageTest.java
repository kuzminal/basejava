package com.kuzmin.storage;

import com.kuzmin.exception.ExistStorageException;
import com.kuzmin.exception.NotExistStorageException;
import com.kuzmin.exception.StorageException;
import com.kuzmin.model.Resume;
import org.junit.jupiter.api.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
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

    @BeforeAll
    public void fillStorage(){
        storage.save(RESUME1);
        storage.save(RESUME2);
        storage.save(RESUME3);
    }

    @Test
    @Order(1)
    void size() {
        Assertions.assertEquals(3, storage.size());
    }

    @Test
    @Order(7)
    void clear() {
        storage.clear();
        Assertions.assertEquals(0, storage.size());
    }

    @Test
    @Order(2)
    void get() {
        Assertions.assertEquals(RESUME1, storage.get(UUID1));
        Assertions.assertEquals(RESUME2, storage.get(UUID2));
        Assertions.assertEquals(RESUME3, storage.get(UUID3));
    }

    @Test
    @Order(3)
    void update() {
        Assertions.assertEquals(RESUME2, storage.get(UUID2));
    }

    @Test
    @Order(6)
    void save() {
        storage.save(RESUME4);
        Assertions.assertEquals(3, storage.size());
        Assertions.assertEquals(RESUME4, storage.get(UUID4));
        Assertions.assertThrows(ExistStorageException.class,() -> {storage.save(RESUME1);});
        Assertions.assertThrows(StorageException.class,() -> {
            for (int i = 3; i <= AbstractArrayStorage.STORAGE_LIMIT; i++) {
                storage.save(new Resume());
            }
        });
    }

    @Test
    @Order(5)
    void delete() {
        storage.delete(UUID3);
        Assertions.assertEquals(2, storage.size());
        Assertions.assertThrows(NotExistStorageException.class,() -> {storage.get(UUID3);});
    }

    @Test
    @Order(4)
    void getAll() {
        Resume[] list = storage.getAll();
        Assertions.assertEquals(3, list.length);
        Assertions.assertEquals(RESUME1, list[0]);
        Assertions.assertEquals(RESUME2, list[1]);
        Assertions.assertEquals(RESUME3, list[2]);
    }
}
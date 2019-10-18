package com.kuzmin.storage;

import com.kuzmin.exception.ExistStorageException;
import com.kuzmin.exception.NotExistStorageException;
import com.kuzmin.model.Resume;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

public abstract class AbstractStorageTest{
    private Storage storage;
    private static final String UUID1 = "uuid1";
    private static final String UUID2 = "uuid2";
    private static final String UUID3 = "uuid3";
    private static final String UUID4 = "uuid4";
    private static final String FULL_NAME1 = "Ivanov Ivan";
    private static final String FULL_NAME2 = "Petrov Petr";
    private static final String FULL_NAME3 = "Sidorov Sidor";
    private static final String FULL_NAME4 = "Kozimirov Kozimir";
    private static final Resume RESUME1 = new Resume(UUID1, FULL_NAME1);
    private static final Resume RESUME2 = new Resume(UUID2, FULL_NAME2);
    private static final Resume RESUME3 = new Resume(UUID3, FULL_NAME3);
    private static final Resume RESUME4 = new Resume(UUID4, FULL_NAME4);

    protected AbstractStorageTest(Storage storage) {
        this.storage = storage;
    }

    @Before
    public void setUp() {
        storage.clear();
        storage.save(RESUME1);
        storage.save(RESUME3);
        storage.save(RESUME2);
    }

    @Test
    public void size() {
        assertEquals(3, storage.size());
    }

    @Test
    public void clear() {
        storage.clear();
        assertEquals(0, storage.size());
    }

    @Test
    public void get() {
        assertEquals(RESUME1, storage.get(RESUME1));
        assertEquals(RESUME2, storage.get(RESUME2));
        assertEquals(RESUME3, storage.get(RESUME3));
    }

    @Test(expected = NotExistStorageException.class)
    public void getNotExist() {
        storage.get(RESUME4);
    }

    @Test
    public void update() {
        Resume testResume = new Resume("uuid2", "Petrov Petr");
        storage.update(testResume);
        assertEquals(RESUME2, storage.get(RESUME2));
    }

    @Test(expected = NotExistStorageException.class)
    public void updateNotExist() {
        storage.update(RESUME4);
    }

    @Test
    public void save() {
        storage.save(RESUME4);
        assertEquals(RESUME4, storage.get(RESUME4));
        assertEquals(4, storage.size());
    }

    @Test(expected = ExistStorageException.class)
    public void saveExist() {
        storage.save(RESUME1);
    }

    @Test
    public void delete() {
        storage.delete(RESUME3);
        assertEquals(2, storage.size());
    }

    @Test(expected = NotExistStorageException.class)
    public void deleteNotExist() {
        storage.get(RESUME4);
    }

    @Test
    public void getAll() {
        List<Resume> resumes = storage.getAllSorted();
        assertEquals(3, resumes.size());
        assertEquals(RESUME1, resumes.get(0));
        assertEquals(RESUME2, resumes.get(1));
        assertEquals(RESUME3, resumes.get(2));
    }
}
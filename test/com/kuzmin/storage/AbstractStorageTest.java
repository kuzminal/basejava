package com.kuzmin.storage;

import com.kuzmin.ResumeTestData;
import com.kuzmin.exception.ExistStorageException;
import com.kuzmin.exception.NotExistStorageException;
import com.kuzmin.model.Resume;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static org.junit.Assert.assertEquals;

public abstract class AbstractStorageTest {
    protected static final File STORAGE_DIR = new File("./storage");
    protected static final Path STORAGE_PATH = Paths.get("./storage");
    protected Storage storage;
    private static final String UUID1 = "uuid1";
    private static final String UUID2 = "uuid2";
    private static final String UUID3 = "uuid3";
    private static final String UUID4 = "uuid4";
    private static final Resume RESUME1 = ResumeTestData.fillResume(UUID1, "Ivanov Ivan");
    private static final Resume RESUME2 = ResumeTestData.fillResume(UUID2, "Petrov Petr");
    private static final Resume RESUME3 = ResumeTestData.fillResume(UUID3, "Sidorov Sidor");
    private static final Resume RESUME4 = ResumeTestData.fillResume(UUID4, "Kozimirov Kozimir");

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
        assertEquals(RESUME1, storage.get(UUID1));
        assertEquals(RESUME2, storage.get(UUID2));
        assertEquals(RESUME3, storage.get(UUID3));
    }

    @Test(expected = NotExistStorageException.class)
    public void getNotExist() {
        storage.get(UUID4);
    }

    @Test
    public void update() {
        Resume testResume = ResumeTestData.fillResume("uuid2", "Petrov Petr");
        storage.update(testResume);
        assertEquals(testResume, storage.get(UUID2));
    }

    @Test(expected = NotExistStorageException.class)
    public void updateNotExist() {
        storage.update(RESUME4);
    }

    @Test
    public void save() {
        storage.save(RESUME4);
        assertEquals(RESUME4, storage.get(UUID4));
        assertEquals(4, storage.size());
    }

    @Test(expected = ExistStorageException.class)
    public void saveExist() {
        storage.save(RESUME1);
    }

    @Test
    public void delete() {
        storage.delete(UUID3);
        assertEquals(2, storage.size());
    }

    @Test(expected = NotExistStorageException.class)
    public void deleteNotExist() {
        storage.get(UUID4);
    }

    @Test
    public void getAllSorted() {
        List<Resume> resumes = storage.getAllSorted();
        assertEquals(3, resumes.size());
        List<Resume> testList = new ArrayList<>();
        testList.add(RESUME1);
        testList.add(RESUME2);
        testList.add(RESUME3);
        testList.sort(Comparator.naturalOrder());
        assertEquals(resumes, testList);
    }
}
package com.kuzmin.storage;

import com.kuzmin.exception.StorageException;
import com.kuzmin.model.Resume;
import org.junit.Test;

import static org.junit.Assert.fail;

public class AbstractArrayStorageTest extends AbstractStorageTest{
    private Storage storage;

    protected AbstractArrayStorageTest(Storage storage) {
        super(storage);
        this.storage = storage;
    }

    @Test(expected = StorageException.class)
    public void saveOverLimit() {
        try {
            for (int i = storage.size(); i < AbstractArrayStorage.STORAGE_LIMIT; i++) {
                storage.save(new Resume());
            }
        } catch (StorageException e) {
            fail();
        }
        storage.save(new Resume());
    }
}

package com.kuzmin.storage;

import com.kuzmin.exception.StorageException;
import com.kuzmin.model.Resume;
import org.junit.Test;

import static org.junit.Assert.fail;

public abstract class AbstractArrayStorageTest extends AbstractStorageTest{
    protected AbstractArrayStorageTest(Storage storage) {
        super(storage);
    }

    @Test(expected = StorageException.class)
    public void saveOverLimit() {
        try {
            for (int i = storage.size(); i < AbstractArrayStorage.STORAGE_LIMIT; i++) {
                storage.save(new Resume("Vasserman"));
            }
        } catch (StorageException e) {
            fail();
        }
        storage.save(new Resume("Vasserman"));
    }
}

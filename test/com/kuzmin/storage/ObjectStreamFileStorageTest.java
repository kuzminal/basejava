package com.kuzmin.storage;

public class ObjectStreamFileStorageTest extends AbstractStorageTest{
    public ObjectStreamFileStorageTest() {
        super(new ObjectStreamFileStorage(STORAGE_DIR));
    }
}
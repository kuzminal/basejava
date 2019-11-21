package com.kuzmin.storage;

import java.io.File;

public class ObjectStreamFileStorageTest extends AbstractStorageTest {
    public ObjectStreamFileStorageTest() {
        super(new ObjectStreamFileStorage(new File(STORAGE_DIR), new ObjectStreamStrategy()));
    }
}
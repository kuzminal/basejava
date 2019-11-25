package com.kuzmin.storage;

import com.kuzmin.storage.serializer.ObjectStreamStrategy;

import java.io.File;

public class ObjectStreamFileStorageTest extends AbstractStorageTest {
    public ObjectStreamFileStorageTest() {
        super(new ObjectStreamFileStorage(new File(STORAGE_DIR), new ObjectStreamStrategy()));
    }
}
package com.kuzmin.storage;

import com.kuzmin.storage.serializer.ObjectStreamStrategy;

import java.nio.file.Paths;

public class ObjectStreamPathStorageTest extends AbstractStorageTest {
    public ObjectStreamPathStorageTest() {
        super(new ObjectStreamPathStorage(Paths.get(STORAGE_DIR), new ObjectStreamStrategy()));
    }
}
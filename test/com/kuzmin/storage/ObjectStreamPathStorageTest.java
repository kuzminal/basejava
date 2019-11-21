package com.kuzmin.storage;

import java.nio.file.Paths;

public class ObjectStreamPathStorageTest extends AbstractStorageTest {
    public ObjectStreamPathStorageTest() {
        super(new ObjectStreamPathStorage(Paths.get(STORAGE_DIR), new ObjectStreamStrategy()));
    }
}
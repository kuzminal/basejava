package com.kuzmin.storage;

import com.kuzmin.storage.serializer.JSONStreamSerializer;

import java.nio.file.Paths;

public class JSONStreamSerializerTest extends AbstractStorageTest {
    public JSONStreamSerializerTest() {
        super(new ObjectStreamPathStorage(Paths.get(STORAGE_DIR), new JSONStreamSerializer()));
    }
}

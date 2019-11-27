package com.kuzmin.storage;

import com.kuzmin.storage.serializer.DataStreamSerializer;

import java.nio.file.Paths;

public class DataStreamSerializerTest extends AbstractStorageTest {
    public DataStreamSerializerTest() {
        super(new ObjectStreamPathStorage(Paths.get(STORAGE_DIR), new DataStreamSerializer()));
    }
}

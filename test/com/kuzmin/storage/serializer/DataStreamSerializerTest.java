package com.kuzmin.storage.serializer;

import com.kuzmin.storage.AbstractStorageTest;
import com.kuzmin.storage.ObjectStreamPathStorage;
import java.nio.file.Paths;

public class DataStreamSerializerTest extends AbstractStorageTest {
    public DataStreamSerializerTest() {
        super(new ObjectStreamPathStorage(Paths.get(STORAGE_DIR), new DataStreamSerializer()));
    }
}

package com.kuzmin.storage.serializer;

import com.kuzmin.storage.AbstractStorageTest;
import com.kuzmin.storage.ObjectStreamPathStorage;

import java.nio.file.Paths;

public class GSONStreamSerializerTest extends AbstractStorageTest {
    public GSONStreamSerializerTest() {
        super(new ObjectStreamPathStorage(Paths.get(STORAGE_DIR), new GSONStreamSerializer()));
    }
}

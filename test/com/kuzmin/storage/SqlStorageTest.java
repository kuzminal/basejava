package com.kuzmin.storage;

import com.kuzmin.Config;

public class SqlStorageTest extends AbstractArrayStorageTest {
    public SqlStorageTest() {
        super(Config.get().getStorage());
    }
}

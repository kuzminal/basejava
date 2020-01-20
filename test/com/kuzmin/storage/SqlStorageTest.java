package com.kuzmin.storage;

import com.kuzmin.Config;

public class SqlStorageTest extends AbstractStorageTest {
    public SqlStorageTest() {
        super(Config.get().getStorage());
    }
}

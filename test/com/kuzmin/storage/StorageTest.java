package com.kuzmin.storage;

import org.junit.jupiter.api.DisplayName;

@DisplayName("Array")
class StorageTest extends AbstractStorageTest {
    public StorageTest() {
        super(new ArrayStorage());
    }
}
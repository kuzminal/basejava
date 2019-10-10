package com.kuzmin.storage;

import static org.junit.jupiter.api.Assertions.*;

class MapStorageTest extends AbstractStorageTest{
    public MapStorageTest() {
        super(new MapStorage());
    }
}
package com.kuzmin.storage;

import org.junit.jupiter.api.BeforeAll;

class SortedArrayStorageTest extends AbstractArrayStorageTest{


    public SortedArrayStorageTest() {
        super(new SortedArrayStorage());
    }

    @BeforeAll
    public static void setUp(){
        new SortedArrayStorageTest();
    }
}
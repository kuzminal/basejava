package com.kuzmin.storage;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        ArrayStorageTest.class,
        MapStorageTest.class,
        SortedArrayStorageTest.class,
        ListStorageTest.class,
        ResumeMapStorageTest.class,
        ObjectStreamFileStorageTest.class,
        ObjectStreamPathStorageTest.class,
        XMLStreamSerializerTest.class,
        JSONStreamSerializerTest.class
})
public class AllStorageTest {

}

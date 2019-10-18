package com.kuzmin.storage;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        StorageTest.class,
        MapStorageTest.class,
        SortedStorageTest.class,
        ListStorageTest.class} )
public class AllStorageTest {

}

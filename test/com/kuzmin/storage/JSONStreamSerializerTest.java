package com.kuzmin.storage;

import com.kuzmin.storage.serializer.JSONStreamSerializer;

import javax.xml.bind.JAXBException;
import java.nio.file.Paths;

public class JSONStreamSerializerTest  extends AbstractStorageTest{
    public JSONStreamSerializerTest() throws JAXBException {
        super(new ObjectStreamPathStorage(Paths.get(STORAGE_DIR), new JSONStreamSerializer()));
    }
}

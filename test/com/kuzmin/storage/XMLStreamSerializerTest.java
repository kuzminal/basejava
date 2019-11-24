package com.kuzmin.storage;

import javax.xml.bind.JAXBException;
import java.nio.file.Paths;

public class XMLStreamSerializerTest extends AbstractStorageTest{
    public XMLStreamSerializerTest() throws JAXBException {
        super(new ObjectStreamPathStorage(Paths.get(STORAGE_DIR), new XMLStreamSerializer()));
    }
}

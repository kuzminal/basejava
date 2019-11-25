package com.kuzmin.storage.serializer;

import com.kuzmin.exception.StorageException;
import com.kuzmin.model.*;
import com.kuzmin.util.XMLParser;

import javax.xml.bind.JAXBException;
import java.io.*;
import java.nio.charset.StandardCharsets;

public class XMLStreamSerializer implements IOStrategy {
    private XMLParser xmlParser;

    public XMLStreamSerializer() throws JAXBException {
        xmlParser = new XMLParser(
                Resume.class, Organization.class, Contact.class, Experience.class,
                TextListSection.class, TextSection.class, OrganizationSection.class
        );
    }

    @Override
    public void doWrite(Resume resume, OutputStream os) {
        try(Writer writer = new OutputStreamWriter(os, StandardCharsets.UTF_8)){
            xmlParser.marshall(resume, writer);
        }catch (IOException | JAXBException e){
            throw new StorageException("Error with XML parsing", null, e);
        }
    }

    @Override
    public Resume doRead(InputStream is) {
        try(Reader reader = new InputStreamReader(is, StandardCharsets.UTF_8)){
            return xmlParser.unmarshall(reader);
        }catch (IOException | JAXBException e){
            throw new StorageException("Error with XML parsing", null, e);
        }
    }
}

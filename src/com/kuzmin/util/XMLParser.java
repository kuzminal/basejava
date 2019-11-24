package com.kuzmin.util;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.Reader;
import java.io.Writer;

public class XMLParser {
    private final Marshaller marshaller;
    private final Unmarshaller unmarshaller;

    public XMLParser(Class... classToSerialie) throws JAXBException {
        JAXBContext context = JAXBContext.newInstance(classToSerialie);
        marshaller = context.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
        marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
        unmarshaller = context.createUnmarshaller();
    }

    public <T> T unmarshall(Reader reader) throws JAXBException {
        return (T) unmarshaller.unmarshal(reader);
    }

    public void marshall(Object instance, Writer writer) throws JAXBException {
        marshaller.marshal(instance, writer);
    }
}

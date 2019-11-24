package com.kuzmin.util;

import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;

public class JSONParser {
    private static ObjectMapper mapper = new ObjectMapper();

    public static  <T> T read(Reader reader, Class classToRead) throws IOException, JsonMappingException {
        //mapper.setDefaultPrettyPrinter(new DefaultPrettyPrinter());
        return (T) mapper.readValue(reader, classToRead);
    }

    public static  <T> void write(T object, Writer writer) throws IOException {
        mapper.setDefaultPrettyPrinter(new DefaultPrettyPrinter());
        mapper.writeValue(writer, object);
    }
}

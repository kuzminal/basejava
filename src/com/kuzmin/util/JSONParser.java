package com.kuzmin.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;

public class JSONParser {
    private static ObjectMapper mapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);

    public static <T> T read(Reader reader, Class classToRead) throws IOException {
        return (T) mapper.readValue(reader, classToRead);
    }

    public static <T> void write(T object, Writer writer) throws IOException {
        mapper.setDefaultPrettyPrinter(new DefaultPrettyPrinter());
        mapper.writeValue(writer, object);
    }

    public static <T> String write(T object) throws JsonProcessingException {
        return mapper.writeValueAsString(object);
    }

    public static <T> T read(String content, Class clas) throws JsonProcessingException {
        return (T) mapper.readValue(content, clas);
    }
}

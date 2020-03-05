package com.kuzmin.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.kuzmin.model.AbstractSection;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;

public class GSONParser {
    private static Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .registerTypeAdapter(AbstractSection.class, new JSONSectionAdapter())
            .create();

    public static  <T> T read(Reader reader, Class classToRead) throws IOException {
        return (T) gson.fromJson(reader, classToRead);
    }

    public static  <T> void write(T object, Writer writer) throws IOException {
        gson.toJson(object, writer);
    }

    public static <T> T read(String content, Class<T> clazz) {
        return gson.fromJson(content, clazz);
    }

    public static <T> String write(T object, Class<T> clazz) {
        return gson.toJson(object, clazz);
    }
}

package com.kuzmin.storage.serializer;

import com.kuzmin.exception.StorageException;
import com.kuzmin.model.Resume;
import com.kuzmin.util.GSONParser;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class GSONStreamSerializer implements IOStrategy{
    @Override
    public void doWrite(Resume resume, OutputStream os) {
        try (Writer writer = new OutputStreamWriter(os, StandardCharsets.UTF_8)) {
            GSONParser.write(resume, writer);
        } catch (IOException e) {
            throw new StorageException("Error with JSON parsing", null, e);
        }
    }

    @Override
    public Resume doRead(InputStream is) {
        try (Reader reader = new InputStreamReader(is, StandardCharsets.UTF_8)){
            return GSONParser.read(reader, Resume.class);
        } catch (IOException e) {
            throw new StorageException("Error with JSON parsing", null, e);
        }
    }
}

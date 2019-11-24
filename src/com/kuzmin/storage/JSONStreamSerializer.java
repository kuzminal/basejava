package com.kuzmin.storage;

import com.kuzmin.exception.StorageException;
import com.kuzmin.model.Resume;
import com.kuzmin.util.JSONParser;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class JSONStreamSerializer implements IOStrategy {
    @Override
    public void doWrite(Resume resume, OutputStream os) {
        try (Writer writer = new OutputStreamWriter(os, StandardCharsets.UTF_8)) {
            JSONParser.write(resume, writer);
        } catch (IOException e) {
            throw new StorageException("Error with XML parsing", null, e);
        }
    }

    @Override
    public Resume doRead(InputStream is) {
        try (Reader reader = new InputStreamReader(is, StandardCharsets.UTF_8)){
            return JSONParser.read(reader, Resume.class);
        } catch (IOException e) {
            throw new StorageException("Error with XML parsing", null, e);
        }
    }
}

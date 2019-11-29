package com.kuzmin.storage.serializer;

import com.kuzmin.model.Resume;
import com.kuzmin.util.GSONParser;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class GSONStreamSerializer implements IOStrategy {
    @Override
    public void doWrite(Resume resume, OutputStream os) throws IOException {
        try (Writer writer = new OutputStreamWriter(os, StandardCharsets.UTF_8)) {
            GSONParser.write(resume, writer);
        }
    }

    @Override
    public Resume doRead(InputStream is) throws IOException {
        try (Reader reader = new InputStreamReader(is, StandardCharsets.UTF_8)) {
            return GSONParser.read(reader, Resume.class);
        }
    }
}

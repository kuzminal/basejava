package com.kuzmin.storage.serializer;

import com.kuzmin.model.Resume;

import java.io.InputStream;
import java.io.OutputStream;

public interface IOStrategy {
    void doWrite(Resume resume, OutputStream os);

    Resume doRead(InputStream is);
}

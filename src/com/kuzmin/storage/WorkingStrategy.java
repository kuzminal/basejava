package com.kuzmin.storage;

import com.kuzmin.model.Resume;

import java.io.IOException;

public interface WorkingStrategy {
    void doWrite(Resume resume, Object os) throws IOException;

    Resume doRead(Object is) throws IOException;
}

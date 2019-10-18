package com.kuzmin.storage;

import com.kuzmin.model.Resume;

import java.util.List;

public interface Storage {
    void clear();

    void update(Resume resume);

    void save(Resume resume);

    Resume get(Resume resume);

    void delete(Resume resume);

    List<Resume> getAllSorted();

    int size();
}

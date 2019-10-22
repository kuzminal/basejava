package com.kuzmin.storage;

import com.kuzmin.exception.ExistStorageException;
import com.kuzmin.exception.NotExistStorageException;
import com.kuzmin.model.Resume;

import java.util.List;
import java.util.stream.Collectors;

public abstract class AbstractStorage implements Storage {

    protected abstract void updateResume(Resume resume, Object searchKey);

    protected abstract void saveResume(Resume resume, Object searchKey);

    protected abstract void deleteResume(Object searchKey);

    protected abstract Object getSearchKey(Object searchKey);

    protected abstract boolean checkSearchKey(Object searchKey);

    protected abstract Resume getResume(Object searchKey);

    protected abstract List<Resume> getAll();

    public List<Resume> sortResumes(List<Resume> resumes){
        return resumes.stream()
                .sorted()
                .collect(Collectors.toList());
    }

    public Resume get(Resume resume) {
        return getResume(getExistedElement(resume));
    }

    public void update(Resume resume) {
        updateResume(resume, getExistedElement(resume));
    }

    public void save(Resume resume) {
        saveResume(resume, getNotExistedSearchKey(resume));
    }

    public void delete(Resume resume) {
        deleteResume(getExistedElement(resume));
    }

    private Object getExistedElement(Resume resume) {
        Object searchKey = getSearchKey(resume);
        if (!checkSearchKey(searchKey)) {
            throw new NotExistStorageException(resume.getUuid());
        }
        return searchKey;
    }

    private Object getNotExistedSearchKey(Resume resume) {
        Object searchKey = getSearchKey(resume);
        if (checkSearchKey(searchKey)) {
            throw new ExistStorageException(resume.getUuid());
        }
        return searchKey;
    }

    public List<Resume> getAllSorted() {
        List<Resume> resumes = getAll();
        return sortResumes(resumes);
    }
}

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

    public Resume get(String uuid) {
        return getResume(getExistedElement(uuid));
    }

    public void update(Resume resume) {
        updateResume(resume, getExistedElement(resume.getUuid()));
    }

    public void save(Resume resume) {
        saveResume(resume, getNotExistedSearchKey(resume.getUuid()));
    }

    public void delete(String resume) {
        deleteResume(getExistedElement(resume));
    }

    public List<Resume> getAllSorted() {
        List<Resume> resumes = getAll();
        return sortResumes(resumes);
    }

    private Object getExistedElement(String uuid) {
        Object searchKey = getSearchKey(uuid);
        if (!checkSearchKey(searchKey)) {
            throw new NotExistStorageException(uuid);
        }
        return searchKey;
    }

    private Object getNotExistedSearchKey(String uuid) {
        Object searchKey = getSearchKey(uuid);
        if (checkSearchKey(searchKey)) {
            throw new ExistStorageException(uuid);
        }
        return searchKey;
    }

    private List<Resume> sortResumes(List<Resume> resumes){
        return resumes.stream()
                .sorted()
                .collect(Collectors.toList());
    }
}

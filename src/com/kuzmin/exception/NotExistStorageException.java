package com.kuzmin.exception;

public class NotExistStorageException extends StorageException {
    public NotExistStorageException(String uuid) {
        super("Resume " + uuid + " is not found in storage.", uuid);
    }
}

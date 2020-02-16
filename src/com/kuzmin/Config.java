package com.kuzmin;

import com.kuzmin.storage.SqlStorage;
import com.kuzmin.storage.Storage;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.Properties;

public class Config {
    protected static final Path PROPS = Paths.get("/Users/aleksejkuzmin/Documents/Development/java/TopJava/basejava/basejava/config/resumes.properties");
    private static final Config INSTANCE = new Config();
    private Properties props = new Properties();
    private String storageDir;
    private Storage storage;

    public static Config get() {
        return INSTANCE;
    }

    private Config() {
        try (InputStream is = new FileInputStream(PROPS.toFile())) {
            props.load(is);
            storageDir = props.getProperty("storage.dir");
            storage = new SqlStorage(props.getProperty("db.url"), props.getProperty("db.user"), props.getProperty("db.password"));
        } catch (SQLException | IOException e) {
            throw new IllegalStateException("Invalid config file " + PROPS.toString());
        }
    }

    public String getStorageDir() {
        return storageDir;
    }

    public Storage getStorage() {
        return storage;
    }
}
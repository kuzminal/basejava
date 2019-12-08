package com.kuzmin.storage.serializer;

import java.io.IOException;

@FunctionalInterface
public interface ReadEachElement<T> {
    void accept() throws IOException;
}

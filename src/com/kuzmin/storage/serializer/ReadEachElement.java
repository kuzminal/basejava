package com.kuzmin.storage.serializer;

import java.io.IOException;

@FunctionalInterface
public interface ReadEachElement {
    void accept() throws IOException;
}

package com.kuzmin.storage.serializer;

import java.io.IOException;

@FunctionalInterface
public interface WriteEachElement {
     void accept(String str) throws IOException;
}

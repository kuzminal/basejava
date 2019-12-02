package com.kuzmin.storage.serializer;

import java.io.IOException;

@FunctionalInterface
public interface WriteEachElement<T> {
     void accept(T collectionElement) throws IOException;
}

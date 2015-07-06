package com.novoda.pxfetcher.task;

public interface Retriever<T extends TagWrapper<V>,V> {
    Result retrieve(T tagWrapper);
}

package com.novoda.pxfetcher;

import com.novoda.pxfetcher.task.Result;
import com.novoda.pxfetcher.task.Retriever;
import com.novoda.pxfetcher.task.Success;
import com.novoda.pxfetcher.task.TagWrapper;

public class FallbackStrategyRetriever<T extends TagWrapper<V>, V> implements Retriever<T, V> {

    private final Retriever<T, V>[] retrievers;

    public FallbackStrategyRetriever(Retriever<T, V>... retrievers) {
        this.retrievers = retrievers;
    }

    @Override
    public Result retrieve(T tagWrapper) {
        for (Retriever<T, V> retriever : retrievers) {
            Result result = retriever.retrieve(tagWrapper);
            if (result instanceof Success) {
                return result;
            }
        }
        return new Failure();
    }

    public static class Failure extends com.novoda.pxfetcher.task.Failure {

    }

}

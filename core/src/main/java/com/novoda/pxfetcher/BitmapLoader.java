package com.novoda.pxfetcher;

import android.annotation.TargetApi;
import android.os.AsyncTask;
import android.os.Build;

import com.novoda.pxfetcher.task.*;

public class BitmapLoader {

    private final Retriever retriever;

    public BitmapLoader(Retriever retriever) {
        this.retriever = retriever;
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void load(TagWrapper tagWrapper, Callback callback) {
        callback.onStart();
        new RetrieverAsyncTask(tagWrapper, retriever)
                .setListener(createListener(callback))
                .executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    private RetrieverAsyncTask.Listener createListener(final Callback callback) {
        return new RetrieverAsyncTask.Listener() {
            @Override
            public void onResult(Result result) {
                result.poke(callback);
            }
        };
    }

    public interface Callback {
        void onStart();
        void onResult(Success ok);
        void onResult(Failure ko);
    }

}

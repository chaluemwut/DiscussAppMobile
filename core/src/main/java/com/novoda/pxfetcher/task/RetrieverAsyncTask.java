package com.novoda.pxfetcher.task;

import android.os.AsyncTask;

public class RetrieverAsyncTask extends AsyncTask<Void, Void, Result> {

    private final TagWrapper tagWrapper;
    private final Retriever retriever;

    private Listener listener;

    public RetrieverAsyncTask(TagWrapper tagWrapper, Retriever retriever) {
        this.tagWrapper = tagWrapper;
        this.retriever = retriever;
    }

    @Override
    protected Result doInBackground(Void... params) {
        return retriever.retrieve(tagWrapper);
    }

    @Override
    protected void onPostExecute(Result result) {
        if (result == null) {
            return;
        }
        if (tagWrapper.isNoLongerValid()) {
            return;
        }

        if (listener != null) {
            listener.onResult(result);
        }
    }

    public RetrieverAsyncTask setListener(Listener listener) {
        this.listener = listener;
        return this;
    }

    public interface Listener {
        void onResult(Result result);
    }
}

package com.novoda.pxfetcher;

import com.novoda.pxfetcher.task.TagWrapper;

public class DefaultTagWrapper extends TagWrapper<Void> {

    public DefaultTagWrapper(String url) {
        super(url);
    }

    @Override
    public Void getMetadata() {
        return null;
    }

}

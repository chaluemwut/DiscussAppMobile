package com.novoda.pxfetcher.task;

import com.novoda.pxfetcher.Tag;

public abstract class TagWrapper<T> {

    public static final int UNSPECIFIED = 0;
    private final Tag tag;

    public TagWrapper(String url) {
        this.tag = new Tag(url);
    }

    public Tag getTag() {
        return tag;
    }

    public String getSourceUrl() {
        return tag.getSourceUrl();
    }

    public boolean isNoLongerValid() {
        return false;
    }

    public int getTargetWidth() {
        return UNSPECIFIED;
    }

    public int getTargetHeight() {
        return UNSPECIFIED;
    }

    public abstract T getMetadata();

}

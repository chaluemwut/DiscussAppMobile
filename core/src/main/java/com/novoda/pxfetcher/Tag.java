package com.novoda.pxfetcher;

import android.widget.ImageView;

public class Tag {
    public static Tag INVALID = new Tag("");
    private final String sourceUrl;
    private Status status;

    public Tag(String sourceUrl) {
        this.sourceUrl = sourceUrl;
        this.status = Status.IDLE;
    }

    public boolean isValid() {
        return this != INVALID;
    }

    public boolean isMarkedAsSuccess() {
        return isValid() && status == Status.SUCCESS;
    }

    public String getSourceUrl() {
        return sourceUrl;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Tag)) {
            return false;
        }

        Tag tag = (Tag) o;

        return sourceUrl.equals(tag.sourceUrl);
    }

    @Override
    public int hashCode() {
        return sourceUrl.hashCode();
    }

    public enum Status {
        IDLE,
        LOADING,
        SUCCESS,
        FAILURE,
        CANCELED;
    }

    public static boolean shouldSkip(String url, ImageView imageView) {
        Tag tag = getTag(imageView);
        return tag.isMarkedAsSuccess() && tag.getSourceUrl().equals(url);
    }

    private static Tag getTag(ImageView imageView) {
        Object o = imageView.getTag();
        if (o == null) {
            return INVALID;
        }

        if (!(o instanceof Tag)) {
            return INVALID;
        }

        return (Tag) o;
    }

    public static void toLoading(ImageView imageView) {
        getTag(imageView).status = Status.LOADING;
    }

    public static void toSuccess(ImageView imageView) {
        getTag(imageView).status = Status.SUCCESS;
    }

    public static void toFailure(ImageView imageView) {
        getTag(imageView).status = Status.FAILURE;
    }

    public static void toCanceled(ImageView imageView) {
        getTag(imageView).status = Status.CANCELED;
    }

}

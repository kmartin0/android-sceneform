package com.kmartin0.sceneformexample.model;

public class SketchfabDownloadInformation {

    private String url;
    private int size;
    private int expires;

    public SketchfabDownloadInformation(String url, int size, int expires) {
        this.url = url;
        this.size = size;
        this.expires = expires;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getExpires() {
        return expires;
    }

    public void setExpires(int expires) {
        this.expires = expires;
    }
}

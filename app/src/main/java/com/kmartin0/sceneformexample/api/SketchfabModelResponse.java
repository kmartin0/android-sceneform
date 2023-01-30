package com.kmartin0.sceneformexample.api;

import com.kmartin0.sceneformexample.model.SketchfabThumbnails;

public class SketchfabModelResponse {

    private String uid;
    private SketchfabThumbnails thumbnails;
    private String name;

    public SketchfabModelResponse(String uid, SketchfabThumbnails thumbnails, String name) {
        this.uid = uid;
        this.thumbnails = thumbnails;
        this.name = name;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public SketchfabThumbnails getThumbnails() {
        return thumbnails;
    }

    public void setThumbnails(SketchfabThumbnails thumbnails) {
        this.thumbnails = thumbnails;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

package com.kmartin0.sceneformexample.model;

import com.kmartin0.sceneformexample.api.SketchfabModelResponse;

import java.util.ArrayList;

public class SketchfabModel {
    private String uid;
    private String name;
    private String thumbnailUrl;

    public SketchfabModel(String uid, String name, String thumbnailUrl) {
        this.uid = uid;
        this.name = name;
        this.thumbnailUrl = thumbnailUrl;
    }

    public SketchfabModel(SketchfabModelResponse sketchfabModelResponse) {
        this.uid = sketchfabModelResponse.getUid();
        this.name = sketchfabModelResponse.getName();

        ArrayList<SketchfabImage> thumbNails = sketchfabModelResponse.getThumbnails().getImages();
        if (thumbNails != null && thumbNails.size() > 0)
            this.thumbnailUrl = sketchfabModelResponse.getThumbnails().getImages().get(0).getUrl();
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }
}

package nl.kmartin.sceneformshowcase.model;

import java.util.ArrayList;

public class SketchfabThumbnails {

    private ArrayList<SketchfabImage> images;

    public SketchfabThumbnails(ArrayList<SketchfabImage> images) {
        this.images = images;
    }

    public ArrayList<SketchfabImage> getImages() {
        return images;
    }

    public void setImages(ArrayList<SketchfabImage> images) {
        this.images = images;
    }
}

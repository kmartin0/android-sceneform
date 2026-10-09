package nl.kmartin.sceneformshowcase.api;

import nl.kmartin.sceneformshowcase.model.SketchfabDownloadInformation;

public class SketchfabModelDownloadResponse {

    private SketchfabDownloadInformation glb;

    public SketchfabModelDownloadResponse(SketchfabDownloadInformation glb) {
        this.glb = glb;
    }

    public SketchfabDownloadInformation getGlb() {
        return glb;
    }

    public void setGlb(SketchfabDownloadInformation glb) {
        this.glb = glb;
    }
}

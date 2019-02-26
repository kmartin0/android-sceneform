package com.kmartin0.sceneformpolyexample.model;

import java.io.Serializable;

public class Thumbnail implements Serializable {
	private String relativePath;
	private String url;
	private String contentType;

	public String getRelativePath() {
		return relativePath;
	}

	public void setRelativePath(String relativePath) {
		this.relativePath = relativePath;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}
}

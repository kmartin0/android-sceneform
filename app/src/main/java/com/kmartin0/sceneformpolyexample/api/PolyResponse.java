package com.kmartin0.sceneformpolyexample.api;


import com.kmartin0.sceneformpolyexample.model.PolyFormat;
import com.kmartin0.sceneformpolyexample.model.Thumbnail;

import java.io.Serializable;

public class PolyResponse implements Serializable {
	private String name;
	private String displayName;
	private String authorName;
	private String description;
	private String createTime;
	private String updateTime;
	private PolyFormat[] formats;
	private Thumbnail thumbnail;

	public String getGltf2Url() {
		if (formats != null) {
			for (PolyFormat format : formats) {
				if (format.getFormatType().equals("GLTF2"))
					return format.getRoot().getUrl();
			}
		}
		return null;
	}

	public String getName() {
		return name;
	}

	public String getDisplayName() {
		return displayName;
	}

	public String getAuthorName() {
		return authorName;
	}

	public String getDescription() {
		return description;
	}

	public String getCreateTime() {
		return createTime;
	}

	public String getUpdateTime() {
		return updateTime;
	}

	public PolyFormat[] getFormats() {
		return formats;
	}

	public Thumbnail getThumbnail() {
		return thumbnail;
	}
}

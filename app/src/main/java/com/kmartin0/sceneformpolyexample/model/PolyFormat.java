package com.kmartin0.sceneformpolyexample.model;

import java.io.Serializable;

public class PolyFormat implements Serializable {

	private FormatRoot root;
	private String formatType;

	public FormatRoot getRoot() {
		return root;
	}

	public void setRoot(FormatRoot root) {
		this.root = root;
	}

	public String getFormatType() {
		return formatType;
	}

	public void setFormatType(String formatType) {
		this.formatType = formatType;
	}
}

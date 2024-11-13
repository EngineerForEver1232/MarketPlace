package com.pedpo.pedporent.widget.customGallery;

import java.io.Serializable;

public class CustomGalleryTO implements Serializable {

	private String sdcardPath;
	private int index ;
	private boolean isSeleted = false;

	public String getSdcardPath() {
		return sdcardPath;
	}

	public void setSdcardPath(String sdcardPath) {
		this.sdcardPath = sdcardPath;
	}

	public boolean isSeleted() {
		return isSeleted;
	}

	public void setSeleted(boolean seleted) {
		isSeleted = seleted;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}
}

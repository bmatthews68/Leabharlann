package com.btmatthews.leabharlann.service.impl;

import java.util.Calendar;

import com.btmatthews.leabharlann.domain.File;

public class FileImpl implements File {

	private String id;
	private String name;
	private String path;
	private String mimeType;
	private String encoding;
	private Calendar lastModified;
	private long size;

	public FileImpl(final String id, final String name, final String path,
			final String mimeType, final String encoding,
			final Calendar lastModified, final long size) {
		this.id = id;
		this.name = name;
		this.path = path;
		this.mimeType = mimeType;
		this.encoding = encoding;
		this.lastModified = lastModified;
		this.size = size;
	}

	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getPath() {
		return path;
	}

	public String getMimeType() {
		return mimeType;
	}

	public String getEncoding() {
		return encoding;
	}

	public Calendar getLastModified() {
		return lastModified;
	}

	public long getSize() {
		return size;
	}
}

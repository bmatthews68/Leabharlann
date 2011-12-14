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

	public FileImpl(final String id, final String name, final String path,
			final String mimeType, final String encoding,
			final Calendar lastModified) {
		this.id = id;
		this.name = name;
		this.path = path;
		this.mimeType = mimeType;
		this.encoding = encoding;
		this.lastModified = lastModified;
	}

	@Override
	public String getId() {
		return id;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getPath() {
		return path;
	}

	@Override
	public String getMimeType() {
		return mimeType;
	}

	@Override
	public String getEncoding() {
		return encoding;
	}

	@Override
	public Calendar getLastModified() {
		return lastModified;
	}
}

package com.btmatthews.leabharlann.service.impl;

import java.util.Map;

import com.btmatthews.leabharlann.domain.Folder;

public class FolderImpl implements Folder {

	private String id;

	private String name;

	private String path;

	public FolderImpl(final String id, final String name, final String path) {
		this.id = id;
		this.name = name;
		this.path = path;
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

	public Map<String, String> getAttributes() {
		return null;
	}

}

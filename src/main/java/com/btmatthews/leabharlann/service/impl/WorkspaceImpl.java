package com.btmatthews.leabharlann.service.impl;

import com.btmatthews.leabharlann.domain.Workspace;

public class WorkspaceImpl implements Workspace {

	private String name;

	public WorkspaceImpl(final String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
}

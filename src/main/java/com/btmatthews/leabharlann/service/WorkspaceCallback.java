package com.btmatthews.leabharlann.service;

import com.btmatthews.leabharlann.domain.Workspace;

public interface WorkspaceCallback {

	Workspace visit(Workspace workspace);
}

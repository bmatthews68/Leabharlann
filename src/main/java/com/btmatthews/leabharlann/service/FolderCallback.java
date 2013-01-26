package com.btmatthews.leabharlann.service;

import com.btmatthews.leabharlann.domain.Folder;

public interface FolderCallback {

	void visit(Folder folder) throws Exception;
}

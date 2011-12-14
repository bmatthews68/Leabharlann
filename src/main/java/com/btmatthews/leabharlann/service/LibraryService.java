package com.btmatthews.leabharlann.service;

import com.btmatthews.leabharlann.domain.Folder;

public interface LibraryService {

	Folder getRoot() throws Exception;

	Folder getFolder(String id) throws Exception;

	void visitFolders(Folder parent, FolderCallback callback) throws Exception;

	void visitFiles(Folder parent, FileCallback callback) throws Exception;
}

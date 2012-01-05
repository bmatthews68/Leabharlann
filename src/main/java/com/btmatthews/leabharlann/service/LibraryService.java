package com.btmatthews.leabharlann.service;

import com.btmatthews.leabharlann.domain.File;
import com.btmatthews.leabharlann.domain.Folder;
import com.btmatthews.leabharlann.domain.Workspace;

public interface LibraryService {

	Workspace getWorkspace(final String workspaceName) throws Exception;

	void visitWorkspaces(WorkspaceCallback callback) throws Exception;

	Folder getRoot(Workspace workspace) throws Exception;

	Folder getFolder(Workspace workspace, String id) throws Exception;

	File getFile(Workspace workspace, String id) throws Exception;

	void visitFolders(Workspace workspace, Folder parent,
			FolderCallback callback) throws Exception;

	void visitFiles(Workspace workspace, Folder parent, FileCallback callback)
			throws Exception;

	void visitFileData(Workspace workspace, File file, FileDataCallback callback)
			throws Exception;
}

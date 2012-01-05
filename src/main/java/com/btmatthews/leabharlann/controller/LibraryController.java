package com.btmatthews.leabharlann.controller;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.btmatthews.leabharlann.domain.File;
import com.btmatthews.leabharlann.domain.Folder;
import com.btmatthews.leabharlann.domain.Workspace;
import com.btmatthews.leabharlann.service.FileCallback;
import com.btmatthews.leabharlann.service.FileDataCallback;
import com.btmatthews.leabharlann.service.FolderCallback;
import com.btmatthews.leabharlann.service.LibraryService;
import com.btmatthews.leabharlann.service.WorkspaceCallback;

@Controller
public class LibraryController {

	private static final String ROOT_UUID = "00000000-0000-0000-0000-000000000000";

	private static final String DEFAULT_WORKSPACE = "default";

	private LibraryService libraryService;

	@Autowired
	public void setLibraryService(final LibraryService service) {
		libraryService = service;
	}

	@RequestMapping(value = "workspaces.json", produces = "application/json")
	public void getWorkspaces(final HttpServletResponse response)
			throws Exception {
		final PrintWriter writer = response.getWriter();
		writer.println("[");
		libraryService.visitWorkspaces(new WorkspaceCallback() {
			public void visit(final Workspace workspace) {
				writer.println("\t{");
				writer.println("\t\tname: '" + workspace.getName() + "',");
				writer.println("\t},");
			}
		});
		writer.println("]");
	}

	@RequestMapping(value = "folders.json", produces = "application/json")
	public void getFolders(
			@RequestParam(value = "workspace", defaultValue = DEFAULT_WORKSPACE) final String workspaceName,
			@RequestParam(value = "node", defaultValue = ROOT_UUID) final String node,
			final HttpServletResponse response) throws Exception {
		final Workspace workspace = libraryService.getWorkspace(workspaceName);
		Folder parent;
		if (ROOT_UUID.equals(node)) {
			parent = libraryService.getRoot(workspace);
		} else {
			parent = libraryService.getFolder(workspace, node);
		}
		final PrintWriter writer = response.getWriter();
		writer.println("[");
		libraryService.visitFolders(workspace, parent, new FolderCallback() {
			public void visit(final Folder folder) {
				writer.println("\t{");
				writer.println("\t\tid: '" + folder.getId() + "',");
				writer.println("\t\ttext: '" + folder.getName() + "',");
				writer.println("\t\tleaf: false,");
				writer.println("\t\tcls: 'folder',");
				writer.println("\t},");
			}
		});
		writer.println("]");
	}

	@RequestMapping(value = "files.json", produces = "application/json")
	public void getFiles(
			@RequestParam(value = "workspace", defaultValue = DEFAULT_WORKSPACE) final String workspaceName,
			@RequestParam(value = "node", defaultValue = ROOT_UUID) final String node,
			@RequestParam(value = "page", required = false) final int page,
			@RequestParam(value = "start", required = false) final int start,
			@RequestParam(value = "limit", required = false) final int limit,
			final HttpServletResponse response) throws Exception {
		final Workspace workspace = libraryService.getWorkspace(workspaceName);
		final SimpleDateFormat dateFormat = new SimpleDateFormat(
				"yyyy-MM-DD HH:mm:ss", response.getLocale());
		Folder folder;
		if (ROOT_UUID.equals(node)) {
			folder = libraryService.getRoot(workspace);
		} else {
			folder = libraryService.getFolder(workspace, node);
		}
		final PrintWriter writer = response.getWriter();
		writer.println("{ files: [");
		libraryService.visitFiles(workspace, folder, new FileCallback() {
			private boolean first = true;

			public void visit(final File file) {
				if (first) {
					first = false;
				} else {
					writer.println(",");
				}
				writer.println("\t{");
				writer.println("\t\tid: '" + file.getId() + "',");
				writer.print("\t\tname: '" + file.getName());
				if (file.getMimeType() != null) {
					writer.println("',");
					writer.print("\t\tmimeType: '");
					writer.print(file.getMimeType());
				}
				if (file.getEncoding() != null) {
					writer.println("',");
					writer.print("\t\tencoding: '");
					writer.print(file.getEncoding());
				}
				if (file.getLastModified() != null) {
					writer.println("',");
					writer.print("\t\tlastModified: '");
					writer.print(dateFormat.format(file.getLastModified()
							.getTime()));
				}
				writer.println("'");
				writer.print("\t}");
			}
		});
		writer.println("");
		writer.println("]}");
	}

	@RequestMapping("File-{workspace}-{nodeId}")
	public void getFile(@PathVariable("workspace") final String workspaceName,
			@PathVariable("nodeId") final String nodeId,
			final HttpServletResponse response) throws Exception {
		final Workspace workspace = libraryService.getWorkspace(workspaceName);
		final File file = libraryService.getFile(workspace, nodeId);
		libraryService.visitFileData(workspace, file, new FileDataCallback() {
			public void visitFileData(final File file,
					final InputStream inputStream) throws Exception {
				response.setStatus(HttpServletResponse.SC_OK);
				response.setContentType(file.getMimeType());
				final String encoding = file.getEncoding();
				if (encoding == null) {
					final OutputStream outputStream = response
							.getOutputStream();
					try {
						IOUtils.copy(inputStream, outputStream);
					} finally {
						IOUtils.closeQuietly(outputStream);
					}
				} else {
					response.setCharacterEncoding(encoding);
					final PrintWriter writer = response.getWriter();
					try {
						IOUtils.copy(inputStream, writer);
					} finally {
						IOUtils.closeQuietly(writer);
					}
				}
			}
		});
	}
}

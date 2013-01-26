package com.btmatthews.leabharlann.controller;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonGenerator;
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
	public void getWorkspaces(final PrintWriter writer)
			throws Exception {
        final JsonFactory factory = new JsonFactory();
        final JsonGenerator generator = factory.createJsonGenerator(writer);
		generator.writeStartArray();
        libraryService.visitWorkspaces(new WorkspaceCallback() {
            public void visit(final Workspace workspace) throws Exception {
                generator.writeStartObject();
                generator.writeStringField("name", workspace.getName());
                generator.writeEndObject();
            }
        });
        generator.writeEndArray();
        generator.close();
	}

	@RequestMapping(value = "folders.json", produces = "application/json")
	public void getFolders(
			@RequestParam(value = "workspace", defaultValue = DEFAULT_WORKSPACE) final String workspaceName,
			@RequestParam(value = "node", defaultValue = ROOT_UUID) final String node,
			final PrintWriter writer) throws Exception {
		final Workspace workspace = libraryService.getWorkspace(workspaceName);
		Folder parent;
		if (ROOT_UUID.equals(node)) {
			parent = libraryService.getRoot(workspace);
		} else {
			parent = libraryService.getFolder(workspace, node);
		}
        final JsonFactory factory = new JsonFactory();
        final JsonGenerator generator = factory.createJsonGenerator(writer);
        generator.writeStartArray();
		libraryService.visitFolders(workspace, parent, new FolderCallback() {
			public void visit(final Folder folder) throws Exception {
                generator.writeStartObject();
                generator.writeStringField("id", folder.getId());
                generator.writeStringField("text", folder.getName());
                generator.writeBooleanField("leaf", false);
                generator.writeStringField("cls", "folder");
                generator.writeEndObject();
			}
		});
        generator.writeEndArray();
        generator.close();
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
		final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-DD HH:mm:ss", response.getLocale());
		final Folder folder;
		if (ROOT_UUID.equals(node)) {
			folder = libraryService.getRoot(workspace);
		} else {
			folder = libraryService.getFolder(workspace, node);
		}
        final JsonFactory factory = new JsonFactory();
        final JsonGenerator generator = factory.createJsonGenerator(response.getWriter());
        generator.writeStartObject();
        generator.writeArrayFieldStart("files");
  		libraryService.visitFiles(workspace, folder, new FileCallback() {
			public void visit(final File file) throws Exception {
                generator.writeStartObject();
                generator.writeStringField("id", file.getId());
                generator.writeStringField("name", file.getName());
				if (file.getMimeType() != null) {
                    generator.writeStringField("mimeType", file.getMimeType());
				}
				if (file.getEncoding() != null) {
                    generator.writeStringField("encoding", file.getEncoding());
				}
				if (file.getLastModified() != null) {
                    generator.writeStringField("lastModified", dateFormat.format(file.getLastModified().getTime()));
				}
                generator.writeEndObject();
			}
		});
		generator.writeEndArray();
		generator.writeEndObject();
        generator.close();
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
                response.addHeader("Content-Disposition", "attachment=" + file.getName());
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

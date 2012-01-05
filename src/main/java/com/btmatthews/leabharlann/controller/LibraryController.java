package com.btmatthews.leabharlann.service;

import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Locale;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.btmatthews.leabharlann.domain.File;
import com.btmatthews.leabharlann.domain.Folder;

@Controller
public class LibraryController {

	private LibraryService libraryService;
	
	@Autowired
	public void setLibraryService(final LibraryService service) {
		libraryService = service;
	}

	@RequestMapping(value = "folders.json", produces = "application/json")
	public void getFolders(@RequestParam("node") final String node,
			final HttpServletResponse response) throws Exception {
		Folder parent;
		if ("00000000-0000-0000-0000-000000000000".equals(node)) {
			parent = libraryService.getRoot();
		} else {
			parent = libraryService.getFolder(node);
		}
		final PrintWriter writer = response.getWriter();
		writer.println("[");
		libraryService.visitFolders(parent, new FolderCallback() {
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
			@RequestParam(value = "node", required = false) final String node,
			@RequestParam(value = "page", required = false) final int page,
			@RequestParam(value = "start", required = false) final int start,
			@RequestParam(value = "limit", required = false) final int limit,
			final HttpServletResponse response) throws Exception {
		final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-DD HH:mm:ss", response.getLocale());
		Folder folder;
		if ("00000000-0000-0000-0000-000000000000".equals(node)) {
			folder = libraryService.getRoot();
		} else {
			folder = libraryService.getFolder(node);
		}
		final PrintWriter writer = response.getWriter();
		writer.println("{ files: [");
		libraryService.visitFiles(folder, new FileCallback() {
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
					writer.print(dateFormat.format(file.getLastModified().getTime()));
				}
				writer.println("'");
				writer.print("\t}");
			}
		});
		writer.println("");
		writer.println("]}");
	}
}

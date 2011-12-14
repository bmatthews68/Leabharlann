package com.btmatthews.leabharlann.service.impl;

import java.util.Calendar;

import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.Property;
import javax.jcr.Repository;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.nodetype.NodeType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.btmatthews.leabharlann.domain.File;
import com.btmatthews.leabharlann.domain.Folder;
import com.btmatthews.leabharlann.service.FileCallback;
import com.btmatthews.leabharlann.service.FolderCallback;
import com.btmatthews.leabharlann.service.LibraryService;

@Service
public class LibraryServiceImpl implements LibraryService {

	private Repository repository;

	@Autowired
	public void setRepositry(final Repository repo) {
		repository = repo;
	}

	public Folder getRoot() throws Exception {
		final Session session = repository.login();
		try {
			final Node root = session.getRootNode();
			return getFolder(root);
		} finally {
			session.logout();
		}
	}

	public Folder getFolder(final String id) throws Exception {
		final Session session = repository.login();
		try {
			final Node node = session.getNodeByIdentifier(id);
			return getFolder(node);
		} finally {
			session.logout();
		}
	}

	private Folder getFolder(final Node node) throws RepositoryException {
		return new FolderImpl(node.getIdentifier(), node.getName(),
				node.getPath());
	}

	private File getFile(final Node node) throws RepositoryException {
		final Node resourceNode = node.getNode(Node.JCR_CONTENT);
		final String mimeType = getStringProperty(resourceNode,
				Property.JCR_MIMETYPE);
		final String encoding = getStringProperty(resourceNode,
				Property.JCR_ENCODING);
		final Calendar lastModified = getDateProperty(resourceNode,
				Property.JCR_LAST_MODIFIED);
		return new FileImpl(node.getIdentifier(), node.getName(),
				node.getPath(), mimeType, encoding, lastModified);
	}

	private String getStringProperty(final Node node, final String name)
			throws RepositoryException {
		if (node.hasProperty(name)) {
			final Property property = node.getProperty(name);
			return property.getString();
		}
		return null;
	}

	private Calendar getDateProperty(final Node node, final String name)
			throws RepositoryException {
		if (node.hasProperty(name)) {
			final Property property = node.getProperty(name);
			return property.getDate();
		}
		return null;
	}

	public void visitFolders(final Folder parent, final FolderCallback callback)
			throws Exception {
		final Session session = repository.login();
		try {
			final Node parentNode = session.getNodeByIdentifier(parent.getId());
			final NodeIterator nodes = parentNode.getNodes();
			while (nodes.hasNext()) {
				final Node node = nodes.nextNode();
				if (node.isNodeType(NodeType.NT_FOLDER)) {
					final Folder folder = getFolder(node);
					callback.visit(folder);
				}
			}
		} finally {
			session.logout();
		}
	}

	public void visitFiles(final Folder parent, final FileCallback callback)
			throws Exception {
		final Session session = repository.login();
		try {
			final Node parentNode = session.getNodeByIdentifier(parent.getId());
			final NodeIterator nodes = parentNode.getNodes();
			while (nodes.hasNext()) {
				final Node node = nodes.nextNode();
				if (node.isNodeType(NodeType.NT_FILE)) {
					final File file = getFile(node);
					callback.visit(file);
				}
			}
		} finally {
			session.logout();
		}
	}
}

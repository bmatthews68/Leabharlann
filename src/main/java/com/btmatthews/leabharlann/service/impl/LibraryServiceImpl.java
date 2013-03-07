package com.btmatthews.leabharlann.service.impl;

import com.btmatthews.leabharlann.domain.File;
import com.btmatthews.leabharlann.domain.Folder;
import com.btmatthews.leabharlann.domain.Workspace;
import com.btmatthews.leabharlann.service.*;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.jcr.*;
import javax.jcr.nodetype.NodeType;
import java.io.InputStream;
import java.util.Calendar;

@Service
public class LibraryServiceImpl implements LibraryService {

    private Repository repository;

    @Autowired
    public void setRepository(final Repository repo) {
        repository = repo;
    }

    public Workspace getWorkspace(final String workspaceName)
            throws Exception {
        return new WorkspaceImpl(workspaceName);
    }

    public void visitWorkspaces(final WorkspaceCallback callback)
            throws Exception {
        final Session session = repository.login();
        try {
            final javax.jcr.Workspace workspace = session.getWorkspace();
            final String[] workspaceNames = workspace
                    .getAccessibleWorkspaceNames();
            for (final String workspaceName : workspaceNames) {
                final Workspace workspaceFolder = getWorkspace(workspaceName);
                callback.visit(workspaceFolder);
            }
        } finally {
            session.logout();
        }
    }

    public Folder getRoot(final Workspace workspace)
            throws Exception {
        final Session session = repository.login(workspace.getName());
        try {
            final Node root = session.getRootNode();
            return getFolder(root);
        } finally {
            session.logout();
        }
    }

    public Folder getFolder(final Workspace workspace,
                            final String id)
            throws Exception {
        final Session session = repository.login(workspace.getName());
        try {
            final Node node = session.getNodeByIdentifier(id);
            return getFolder(node);
        } finally {
            session.logout();
        }
    }

    public Folder createFolder(final Workspace workspace,
                               final String parentId,
                               final String name)
            throws RepositoryException {
        final Session session = repository.login(workspace.getName());
        try {
            final Node parentNode = session.getNodeByIdentifier(parentId);
            final Node folderNode = parentNode.addNode(name, NodeType.NT_FOLDER);
            session.save();
            return getFolder(folderNode);
        } finally {
            session.logout();
        }
    }

    private Folder getFolder(final Node node) throws RepositoryException {
        return new FolderImpl(node.getIdentifier(), node.getName(),
                node.getPath());
    }

    public File getFile(final Workspace workspace, final String id)
            throws Exception {
        final Session session = repository.login(workspace.getName());
        try {
            final Node node = session.getNodeByIdentifier(id);
            return getFile(node);
        } finally {
            session.logout();
        }
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

    public void visitFolders(final Workspace workspace,
                             final Folder parent,
                             final FolderCallback callback)
            throws Exception {

        final Session session = repository.login(workspace.getName());
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

    public void visitFiles(final Workspace workspace, final Folder parent,
                           final FileCallback callback) throws Exception {
        final Session session = repository.login(workspace.getName());
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

    public void visitFileData(final Workspace workspace, final File file,
                              final FileDataCallback callback) throws Exception {
        final Session session = repository.login(workspace.getName());
        try {
            final Node fileNode = session.getNodeByIdentifier(file.getId());
            final Node resourceNode = fileNode.getNode(Node.JCR_CONTENT);
            final Property dataProperty = resourceNode
                    .getProperty(Property.JCR_DATA);
            final Binary data = dataProperty.getBinary();
            final InputStream dataStream = data.getStream();
            try {
                callback.visitFileData(file, dataStream);
            } finally {
                IOUtils.closeQuietly(dataStream);
            }
        } finally {
            session.logout();
        }
    }
}

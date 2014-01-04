/*
 * Copyright 2012-2014 Brian Matthews
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.btmatthews.leabharlann.service.impl;

import com.btmatthews.atlas.jcr.JCRAccessor;
import com.btmatthews.atlas.jcr.NodeCallback;
import com.btmatthews.atlas.jcr.NodeVoidCallback;
import com.btmatthews.atlas.jcr.SessionCallback;
import com.btmatthews.leabharlann.domain.File;
import com.btmatthews.leabharlann.domain.FileContent;
import com.btmatthews.leabharlann.domain.Folder;
import com.btmatthews.leabharlann.domain.Workspace;
import com.btmatthews.leabharlann.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.jcr.*;
import javax.jcr.nodetype.NodeType;
import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * An implementation of the {@link LibraryService} that accesses and manipulates the contents of a Java Content
 * Repository using a {@link JCRAccessor} API object.
 *
 * @author <a href="mailto:brian@btmatthews.com">Brian Matthews</a>
 * @since 1.0.0
 */
@Service
public class LibraryServiceImpl implements LibraryService {

    /**
     * The callback used to transform repository nodes into {@link Folder} descriptors.
     */
    private FolderNodeCallback folderNodeCallback = new FolderNodeCallback();
    /**
     * The callback used to transform repository nodes into {@link File} descriptors.
     */
    private FileNodeCallback fileNodeCallback = new FileNodeCallback();
    /**
     * The {@link JCRAccessor} API object used to access the Java Content Repository.
     */
    private final JCRAccessor jcrAccessor;
    /**
     * Used to determine the file's content (MIME) type.
     */
    private final TypeDetector typeDetector;
    /**
     * Used to determine the file's content encoding.
     */
    private final EncodingDetector encodingDetector;

    /**
     * Inject the {@link JCRAccessor} API object used to access the Java Content repository.
     *
     * @param jcrAccessor      The {@link JCRAccessor} API object.
     * @param typeDetector     Used to determine the file's content (MIME) type.
     * @param encodingDetector Used to determine the file's content encoding.
     */
    @Autowired
    public LibraryServiceImpl(final JCRAccessor jcrAccessor,
                              final TypeDetector typeDetector,
                              final EncodingDetector encodingDetector) {
        this.jcrAccessor = jcrAccessor;
        this.typeDetector = typeDetector;
        this.encodingDetector = encodingDetector;
    }

    /**
     * Get the {@link Workspace} descriptor for the workspace named {@code workspaceName}.
     *
     * @param workspaceName The workspace name.
     * @return The {@link Workspace} descriptor.
     */
    public Workspace getWorkspace(final String workspaceName) {
        return new WorkspaceImpl(workspaceName);
    }

    /**
     * Get a list of {@link Workspace} descriptors for all the workspaces in the Java Content Repository.
     *
     * @return A list of {@link Workspace} descriptors.
     */
    public List<Workspace> getWorkspaces() {
        return jcrAccessor.withSession("default", new SessionCallback<List<Workspace>>() {
            @Override
            public List<Workspace> doInSession(Session session) throws RepositoryException {
                final List<Workspace> workspaces = new ArrayList<Workspace>();
                final javax.jcr.Workspace workspace = session.getWorkspace();
                final String[] workspaceNames = workspace.getAccessibleWorkspaceNames();
                for (final String workspaceName : workspaceNames) {
                    final Workspace workspaceFolder = getWorkspace(workspaceName);
                    workspaces.add(workspaceFolder);
                }
                return workspaces;
            }
        });
    }

    /**
     * Get the {@link Folder} descriptor for the root folder of the workspace described by {@code workspace}.
     *
     * @param workspace The {@link Workspace} descriptor.
     * @return The {@link Folder} descriptor.
     */
    public Folder getRoot(final Workspace workspace) {
        return jcrAccessor.withRoot(workspace.getName(), folderNodeCallback);
    }

    /**
     * Create a new folder named {@code name} in the workspace described by {@code workspace} under the folder
     * identified by {@code parentId}.
     *
     * @param workspace The {@link Workspace} descriptor.
     * @param parentId  The node identifier of the parent folder.
     * @param name      The name for the new child folder.
     * @return The {@link Folder} descriptor for the newly created folder.
     */
    public Folder createFolder(final Workspace workspace,
                               final String parentId,
                               final String name) {
        return jcrAccessor.withNodeId(workspace.getName(), parentId, new NodeCallback<Folder>() {
            @Override
            public Folder doInSessionWithNode(Session session, Node node) throws RepositoryException {
                final Node folder = node.addNode(name, NodeType.NT_FOLDER);
                session.save();
                return folderNodeCallback.doInSessionWithNode(session, folder);
            }
        });
    }

    /**
     * Get the folder identified by {@code id} in the workspace described by {@code workspace}.
     *
     * @param workspace The {@link Workspace} descriptor.
     * @param id        The node identifier of the folder.
     * @return The {@link Folder} descriptor.
     */
    public Folder getFolder(final Workspace workspace,
                            final String id) {
        return jcrAccessor.withNodeId(workspace.getName(), id, folderNodeCallback);
    }

    /**
     * Get the file identified by {@code id} in the workspace described by {@code workspace}.
     *
     * @param workspace The {@link Workspace} descriptor.
     * @param id        The node identifier of the file.
     * @return The {@link File} descriptor.
     */
    public File getFile(final Workspace workspace,
                        final String id) {
        return jcrAccessor.withNodeId(workspace.getName(), id, fileNodeCallback);
    }

    /**
     * Get a list of {@link Folder} descriptors for the sub-folders of the folder described by {@code parent}.
     *
     * @param workspace The {@link Workspace} descriptor.
     * @param parent    The {@link Folder} descriptor of the parent folder.
     * @return A list containing a {@link Folder} descriptor for each sub-folder.
     */
    public List<Folder> getFolders(final Workspace workspace,
                                   final Folder parent) {
        final List<Folder> folders = new ArrayList<Folder>();
        jcrAccessor.withNodeId(workspace.getName(), parent.getId(), new NodeVoidCallback() {
            @Override
            public void doInSessionWithNode(final Session session, final Node node) throws RepositoryException {
                final NodeIterator nodes = node.getNodes();
                while (nodes.hasNext()) {
                    final Node child = nodes.nextNode();
                    if (child.isNodeType(NodeType.NT_FOLDER)) {
                        final Folder folder = folderNodeCallback.doInSessionWithNode(session, child);
                        folders.add(folder);
                    }
                }
            }
        });
        return folders;
    }

    /**
     * Get a list of {@link File} descriptors for the file in the folder described by {@code parent}.
     *
     * @param workspace The {@link Workspace} descriptor.
     * @param parent    The {@link Folder} descriptor of the parent folder.
     * @return A list containing a {@link File} descriptor for each file.
     */
    public List<File> getFiles(final Workspace workspace,
                               final Folder parent) {
        final List<File> files = new ArrayList<File>();
        jcrAccessor.withNodeId(workspace.getName(), parent.getId(), new NodeVoidCallback() {
            @Override
            public void doInSessionWithNode(final Session session, final Node node) throws RepositoryException {
                final NodeIterator nodes = node.getNodes();
                while (nodes.hasNext()) {
                    final Node child = nodes.nextNode();
                    if (node.isNodeType(NodeType.NT_FILE)) {
                        final File file = fileNodeCallback.doInSessionWithNode(session, child);
                        files.add(file);
                    }
                }
            }
        });
        return files;
    }

    /**
     * Get a {@link FileContent} descriptor for the file described by {@code file}.
     *
     * @param workspace The {@link Workspace} descriptor.
     * @param file      The {@link File} descriptor.
     * @return The {@link FileContent} descriptor.
     */
    public FileContent getFileContent(final Workspace workspace,
                                      final File file) {
        return new FileContentImpl(workspace.getName(), file.getId());
    }

    /**
     * Import contents into the repository.
     *
     * @param workspace The target workspace in the repository.
     * @param parent    The target directory in the repository.
     * @param source    The import source.
     */
    public void importContents(final Workspace workspace,
                               final Folder parent,
                               final ImportSource source) {
        jcrAccessor.withNodeId(
                workspace.getName(),
                parent.getId(),
                new ImportContentsCallback(source));
    }

    /**
     * Callback that creates a {@link Folder} descriptor for matching repository nodes.
     */
    private static class FolderNodeCallback implements NodeCallback<Folder> {

        /**
         * Called when a repository node is matched.
         *
         * @param session The repository session.
         * @param node    The matching folder node.
         * @return A {@link Folder} file descriptor.
         * @throws RepositoryException If there was an error accessing the node properties.
         */
        @Override
        public Folder doInSessionWithNode(final Session session,
                                          final Node node)
                throws RepositoryException {
            return new FolderImpl(node.getIdentifier(), node.getName(),
                    node.getPath());
        }
    }

    private class ImportContentsCallback implements NodeVoidCallback {

        private final ImportSource source;

        ImportContentsCallback(final ImportSource source) {
            this.source = source;
        }

        @Override
        public void doInSessionWithNode(final Session session,
                                        final Node node)
                throws Exception {
            source.process(new ImportContentsSourceCallback(session, node));
            session.save();
        }
    }

    private class ImportContentsSourceCallback implements ImportCallback {

        private final Session session;
        private final Node node;

        ImportContentsSourceCallback(final Session session,
                                     final Node node) {
            this.session = session;
            this.node = node;
        }

        @Override
        public void directory(final String path) throws Exception {
            if (!"/".equals(path)) {
                final String[] pathElements = StringUtils.split(path, "/");
                Node currentNode = node;
                for (final String pathElement : pathElements) {
                    if (currentNode.hasNode(pathElement)) {
                        currentNode = currentNode.getNode(pathElement);
                    } else {
                        currentNode = currentNode.addNode(pathElement, NodeType.NT_FOLDER);
                    }
                }
            }
        }

        @Override
        public void file(final String path,
                         final long lastModified,
                         final byte[] contents)
                throws Exception {
            final String[] pathElements = StringUtils.split(path, "/");
            Node currentNode = node;
            for (int i = 0; i < pathElements.length - 1; i++) {
                if (currentNode.hasNode(pathElements[i])) {
                    currentNode = currentNode.getNode(pathElements[i]);
                } else {
                    currentNode = currentNode.addNode(pathElements[i], NodeType.NT_FOLDER);
                }
            }
            final String filename = pathElements[pathElements.length - 1];
            currentNode = currentNode.addNode(filename, NodeType.NT_FILE);
            currentNode = currentNode.addNode(Node.JCR_CONTENT, NodeType.NT_RESOURCE);
            final String encoding = encodingDetector.detect(filename, contents);
            if (encoding != null) {
                currentNode.setProperty(Property.JCR_ENCODING, encoding);
            }
            final String mimeType = typeDetector.detect(filename, contents);
            if (mimeType != null) {
                currentNode.setProperty(Property.JCR_MIMETYPE, mimeType);
            }
            final Calendar lastModifiedDate = Calendar.getInstance();
            lastModifiedDate.setTimeInMillis(lastModified);
            currentNode.setProperty(Property.JCR_LAST_MODIFIED, lastModifiedDate);
            final ValueFactory valueFactory = session.getValueFactory();
            final Binary data = valueFactory.createBinary(new ByteArrayInputStream(contents));
            currentNode.setProperty(Property.JCR_DATA, data);
        }
    }

    /**
     * Callback that creates {@link File} descriptor for matching repository nodes.
     */
    private class FileNodeCallback implements NodeCallback<File> {

        /**
         * Called when a repository node is matched.
         *
         * @param session The repository session.
         * @param node    The matching file node.
         * @return A {@link File} file descriptor.
         * @throws RepositoryException If there was an error accessing the node properties.
         */
        @Override
        public File doInSessionWithNode(final Session session,
                                        final Node node)
                throws RepositoryException {
            final Node resourceNode = node.getNode(Node.JCR_CONTENT);
            final String mimeType = jcrAccessor.getStringProperty(resourceNode,
                    Property.JCR_MIMETYPE);
            final String encoding = jcrAccessor.getStringProperty(resourceNode,
                    Property.JCR_ENCODING);
            final Calendar lastModified = jcrAccessor.getCalendarProperty(resourceNode,
                    Property.JCR_LAST_MODIFIED);
            final Binary data = jcrAccessor.getBinaryProperty(resourceNode, Property.JCR_DATA);
            return new FileImpl(node.getIdentifier(), node.getName(),
                    node.getPath(), mimeType, encoding, data.getSize(), lastModified);
        }
    }
}

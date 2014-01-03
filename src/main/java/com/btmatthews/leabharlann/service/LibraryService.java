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

package com.btmatthews.leabharlann.service;

import com.btmatthews.leabharlann.domain.File;
import com.btmatthews.leabharlann.domain.FileContent;
import com.btmatthews.leabharlann.domain.Folder;
import com.btmatthews.leabharlann.domain.Workspace;

import java.util.List;

/**
 * Defines the interface for services that access and manipulate the contents of a content repository.
 *
 * @author <a href="mailto:brian@btmatthews.com">Brian Matthews</a>
 * @since 1.0.0
 */
public interface LibraryService {

    /**
     * Get the {@link Workspace} descriptor for the workspace named {@code workspaceName}.
     *
     * @param workspaceName The workspace name.
     * @return The {@link Workspace} descriptor.
     */
    Workspace getWorkspace(final String workspaceName);

    /**
     * Get a list of {@link Workspace} descriptors for all the workspaces in the Java Content Repository.
     *
     * @return A list of {@link Workspace} descriptors.
     */
    List<Workspace> getWorkspaces();

    /**
     * Get the {@link Folder} descriptor for the root folder of the workspace described by {@code workspace}.
     *
     * @param workspace The {@link Workspace} descriptor.
     * @return The {@link Folder} descriptor.
     */
    Folder getRoot(Workspace workspace);

    /**
     * Create a new folder named {@code name} in the workspace described by {@code workspace} under the folder
     * identified by {@code parentId}.
     *
     * @param workspace The {@link Workspace} descriptor.
     * @param parentId  The node identifier of the parent folder.
     * @param name      The name for the new child folder.
     * @return The {@link Folder} descriptor for the newly created folder.
     */
    Folder createFolder(Workspace workspace,
                        String parentId,
                        String name);

    /**
     * Get the folder identified by {@code id} in the workspace described by {@code workspace}.
     *
     * @param workspace The {@link Workspace} descriptor.
     * @param id        The node identifier of the folder.
     * @return The {@link Folder} descriptor.
     */
    Folder getFolder(Workspace workspace,
                     String id);

    /**
     * Get the file identified by {@code id} in the workspace described by {@code workspace}.
     *
     * @param workspace The {@link Workspace} descriptor.
     * @param id        The node identifier of the file.
     * @return The {@link File} descriptor.
     */
    File getFile(Workspace workspace,
                 String id);

    /**
     * Get a list of {@link Folder} descriptors for the sub-folders of the folder described by {@code parent}.
     *
     * @param workspace The {@link Workspace} descriptor.
     * @param parent    The {@link Folder} descriptor of the parent folder.
     * @return A list containing a {@link Folder} descriptor for each sub-folder.
     */
    List<Folder> getFolders(Workspace workspace,
                            Folder parent);

    /**
     * Get a list of {@link File} descriptors for the file in the folder described by {@code parent}.
     *
     * @param workspace The {@link Workspace} descriptor.
     * @param parent    The {@link Folder} descriptor of the parent folder.
     * @return A list containing a {@link File} descriptor for each file.
     */
    List<File> getFiles(Workspace workspace,
                        Folder parent);

    /**
     * Get a {@link FileContent} descriptor for the file described by {@code file}.
     *
     * @param workspace The {@link Workspace} descriptor.
     * @param file      The {@link File} descriptor.
     * @return The {@link FileContent} descriptor.
     */
    FileContent getFileContent(Workspace workspace,
                               File file);

    /**
     * Import contents into the repository.
     *
     * @param workspace The target workspace in the repository.
     * @param parent    The target directory in the repository.
     * @param source    The import source.
     */
    void importContents(Workspace workspace,
                        Folder parent,
                        ImportSource source);
}

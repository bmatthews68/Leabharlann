/*
 * Copyright 2012-2013 Brian Matthews
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

package com.btmatthews.leabharlann.controller;

import com.btmatthews.leabharlann.domain.File;
import com.btmatthews.leabharlann.domain.FileContent;
import com.btmatthews.leabharlann.domain.Folder;
import com.btmatthews.leabharlann.domain.Workspace;
import com.btmatthews.leabharlann.service.LibraryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * A Spring MVC controller that implements the endpoints for the RESTful interface to the content repository.
 *
 * @author <a href="mailto:brian@btmatthews.com">Brian Matthews</a>
 * @since 1.0.0
 */
@Controller
public class LibraryController {

    /**
     * Dummy node identifier for the root repository.
     */
    private static final String ROOT_UUID = "00000000-0000-0000-0000-000000000000";
    /**
     * The service bean used to access and manipulate the content repository.
     */
    private LibraryService libraryService;

    /**
     * Inject the service bean used to access and manipulate the content repository.
     *
     * @param service The service bean.
     */
    @Autowired
    public void setLibraryService(final LibraryService service) {
        libraryService = service;
    }

    /**
     * The endpoint that returns a list of workspace descriptors.
     *
     * @return A list of {@link Workspace} descriptors.
     */
    @RequestMapping(value = "workspaces", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<Workspace> getWorkspaces() {
        return libraryService.getWorkspaces();
    }

    /**
     * The endpoint that creates a new sub-folder in a workspace.
     *
     * @param workspaceName The workspace name.
     * @param node          The node identifier of the parent folder.
     * @param folder        The {@link Folder} descriptor for the new folder.
     * @return The {@link Folder} for the newly created node.
     */
    @RequestMapping(value = "workspaces/{workspace}/folders/{node}/folder", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public Folder createFolder(@PathVariable("workspace") final String workspaceName,
                               @PathVariable("node") final String node,
                               @RequestBody final Folder folder) {
        final Workspace workspace = libraryService.getWorkspace(workspaceName);
        return libraryService.createFolder(workspace, node, folder.getName());
    }

    /**
     * The endpoint that returns a list of sub-folders in a folder.
     *
     * @param workspaceName The workspace name.
     * @param node          The node identifier of the parent folder.
     * @return A list of {@link Folder} descriptors.
     */
    @RequestMapping(value = "workspaces/{workspace}/folders/{node}/folders", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<Folder> getFolders(@PathVariable("workspace") final String workspaceName,
                                   @PathVariable("node") final String node) {
        final Workspace workspace = libraryService.getWorkspace(workspaceName);
        Folder parent;
        if (ROOT_UUID.equals(node)) {
            parent = libraryService.getRoot(workspace);
        } else {
            parent = libraryService.getFolder(workspace, node);
        }
        return libraryService.getFolders(workspace, parent);
    }

    /**
     * The endpoint that returns a list of files in a folder.
     *
     * @param workspaceName The workspace name.
     * @param node          The node identifier of the parent folder.
     * @return A list of {@link File} descriptors.
     */
    @RequestMapping(value = "workspaces/{workspace}/folders/{node}/files", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<File> getFiles(@PathVariable("workspace") final String workspaceName,
                               @PathVariable("node") final String node) {
        final Workspace workspace = libraryService.getWorkspace(workspaceName);
        final Folder folder;
        if (ROOT_UUID.equals(node)) {
            folder = libraryService.getRoot(workspace);
        } else {
            folder = libraryService.getFolder(workspace, node);
        }
        return libraryService.getFiles(workspace, folder);
    }

    /**
     * The endpoint that returns the content of a file. The {@link com.btmatthews.leabharlann.view.FileContentMessageConverter}
     * message converter will retrieve the file content from the repository and marshal into the servlet response's output
     * stream.
     *
     * @param workspaceName The workspace name.
     * @param nodeId        The node identifier of the file.
     * @return A {@link FileContent} descriptor.
     */
    @RequestMapping("workspaces/{workspace}/files/{nodeId}/contents")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public FileContent getFile(@PathVariable("workspace") final String workspaceName,
                               @PathVariable("nodeId") final String nodeId) {
        final Workspace workspace = libraryService.getWorkspace(workspaceName);
        final File file = libraryService.getFile(workspace, nodeId);
        return libraryService.getFileContent(workspace, file);
    }
}

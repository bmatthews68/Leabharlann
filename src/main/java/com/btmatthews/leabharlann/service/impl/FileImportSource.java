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

import com.btmatthews.leabharlann.service.ImportCallback;
import com.btmatthews.leabharlann.service.ImportSource;
import org.apache.commons.io.FileUtils;

import java.io.File;

/**
 * Import content from a file system location.
 *
 * @author <a href="mailto:brian@btmatthews.com">Brian Matthews</a>
 * @since 1.0.0
 */
public class FileImportSource implements ImportSource {

    /**
     * The content root.
     */
    private final File file;

    /**
     * Initialize the import tool with a content root.
     *
     * @param file The content root.
     */
    public FileImportSource(final File file) {
        this.file = file;
    }

    /**
     * If the content root is a file then the file is processed. Otherwise, if the content
     * root is a directory a depth first traversal of the directory tree is preformed
     * processing each file discovered.
     *
     * @param callback The callback used to process the files.
     * @throws Exception If there was an error.
     */
    @Override
    public void process(final ImportCallback callback) throws Exception {
        if (file.isDirectory()) {
            processDirectory(file, callback);
        } else {
            final byte[] data = FileUtils.readFileToByteArray(file);
            callback.file(File.separator + file.getName(), file.lastModified(), data);
        }
    }

    /**
     * Perform a depth first traversal of the directory tree with a root at {@code current}
     * processing each file discovered by invoking {@code callback}.
     * root is a directory a depth first traversal of the directory tree is preformed
     * processing each file discovered.
     *
     * @param current The root directory.
     * @param callback The callback used to process the files.
     * @throws Exception If there was an error.
     */
    private void processDirectory(final File current,
                                  final ImportCallback callback)
            throws Exception {
        for (final File child : current.listFiles()) {
            final String path = File.separator + file.toURI().relativize(child.toURI()).getPath();
            if (child.isDirectory()) {
                callback.directory(path);
                processDirectory(child, callback);
            } else {
                final byte[] data = FileUtils.readFileToByteArray(child);
                callback.file(path, child.lastModified(), data);
            }
        }
    }
}

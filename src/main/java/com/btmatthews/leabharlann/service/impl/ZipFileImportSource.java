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
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * Import content from a ZIP archive.
 *
 * @author <a href="mailto:brian@btmatthews.com">Brian Matthews</a>
 * @since 1.0.0
 */
public class ZipFileImportSource implements ImportSource {

    /**
     * The input stream used to read the ZIP archive contents.
     */
    private final ZipInputStream zipInputStream;

    /**
     * Initialize the import tool with the ZIP archive location.
     *
     * @param file The path to the ZIP archive location.
     * @throws IOException If there was a problem opening the ZIP archive.
     */
    public ZipFileImportSource(final String file) throws IOException {
        this(new File(file));
    }

    /**
     * Initialize the import tool with the ZIP archive.
     *
     * @param file The ZIP archive.
     * @throws IOException If there was a problem opening the ZIP archive.
     */
    public ZipFileImportSource(final File file) throws IOException {
        this(new FileInputStream(file));
    }

    /**
     * Initialize the import tool with an input stream.
     *
     * @param inputStream The input stream used to read the ZIP archive contents.
     */
    public ZipFileImportSource(final InputStream inputStream) {
        zipInputStream = new ZipInputStream(inputStream);
    }

    /**
     * Iterate through the contents of the ZIP archive processing the files encountered.
     *
     * @param callback Used to process files.
     * @throws Exception If there was a problem reading from the ZIP archive.
     */
    @Override
    public void process(final ImportCallback callback)
            throws Exception {

        for (ZipEntry entry = zipInputStream.getNextEntry(); entry != null; entry = zipInputStream.getNextEntry()) {
            final String path = entry.getName();
            if (path.endsWith(File.separator)) {
                callback.directory(path);
            } else {
                final long size = entry.getSize();
                final long lastModified = entry.getTime();
                final byte[] contents = IOUtils.toByteArray(zipInputStream, size);
                callback.file(path, lastModified, contents);
            }
        }
    }
}

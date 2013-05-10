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

package com.btmatthews.leabharlann.service.impl;

import com.btmatthews.leabharlann.domain.File;

import java.util.Calendar;

/**
 * An implementation of the {@link File} descriptor.
 *
 * @author <a href="mailto:brian@btmatthews.com">Brian Matthews</a>
 * @since 1.0.0
 */
public class FileImpl implements File {

    /**
     * The file identifier.
     */
    private String id;
    /**
     * The file name.
     */
    private String name;
    /**
     * The file path.
     */
    private String path;
    /**
     * The file's MIME type.
     */
    private String mimeType;
    /**
     * The file's character encoding.
     */
    private String encoding;
    /**
     * The file size.
     */
    private long size;
    /**
     * The file's last modified date.
     */
    private Calendar lastModified;

    /**
     * Initialize the file descriptor.
     *
     * @param id           The file identifier.
     * @param name         The file name.
     * @param path         The file path.
     * @param mimeType     The file's MIME type.
     * @param encoding     The file's character encoding.
     * @param size         The file size.
     * @param lastModified The file's last modified date.
     */
    public FileImpl(final String id, final String name, final String path,
                    final String mimeType, final String encoding, final long size,
                    final Calendar lastModified) {
        this.id = id;
        this.name = name;
        this.path = path;
        this.mimeType = mimeType;
        this.encoding = encoding;
        this.size = size;
        this.lastModified = lastModified;
    }

    /**
     * Get the file identifier.
     *
     * @return The file identifier.
     */
    public String getId() {
        return id;
    }

    /**
     * Get the file name.
     *
     * @return The file name.
     */
    public String getName() {
        return name;
    }

    /**
     * Get the file path.
     *
     * @return The file path.
     */
    public String getPath() {
        return path;
    }

    /**
     * Get the file's MIME type.
     *
     * @return The file's MIME type.
     */
    public String getMimeType() {
        return mimeType;
    }

    /**
     * Get the file's character encoding.
     *
     * @return The file's character encoding.
     */
    public String getEncoding() {
        return encoding;
    }

    /**
     * Get the file size.
     *
     * @return The file size.
     */
    public long getSize() {
        return size;
    }

    /**
     * Get the file's last modified date.
     *
     * @return The file's last modified date.
     */
    public Calendar getLastModified() {
        return lastModified;
    }
}

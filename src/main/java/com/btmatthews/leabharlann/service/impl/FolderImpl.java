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

import com.btmatthews.leabharlann.domain.Folder;

import java.util.Map;

/**
 * An implementation of the {@link Folder} descriptor.
 *
 * @author <a href="mailto:brian@btmatthews.com">Brian Matthews</a>
 * @since 1.0.0
 */
public class FolderImpl implements Folder {

    /**
     * The folder identifier.
     */
    private String id;
    /**
     * The folder name.
     */
    private String name;
    /**
     * The folder path.
     */
    private String path;

    /**
     * Initialize the folder descriptor.
     *
     * @param id   The folder identifier.
     * @param name The folder name.
     * @param path The folder path.
     */
    public FolderImpl(final String id,
                      final String name,
                      final String path) {
        this.id = id;
        this.name = name;
        this.path = path;
    }

    /**
     * Get the folder identifier.
     *
     * @return The folder identifier.
     */
    public String getId() {
        return id;
    }

    /**
     * Get the folder name.
     *
     * @return The folder name.
     */
    public String getName() {
        return name;
    }

    /**
     * Get the folder path.
     *
     * @return The folder path.
     */
    public String getPath() {
        return path;
    }

    /**
     * Get the folder attributes.
     *
     * @return The folder attributes.
     */
    public Map<String, String> getAttributes() {
        return null;
    }
}

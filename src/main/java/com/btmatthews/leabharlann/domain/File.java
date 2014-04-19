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

package com.btmatthews.leabharlann.domain;

import java.util.Calendar;

/**
 * Describes a file in the content repository.
 *
 * @author <a href="mailto:brian@btmatthews.com">Brian Matthews</a>
 * @since 1.0.0
 */
public interface File {

    /**
     * Get the file identifier.
     *
     * @return The file identifier.
     */
    String getId();

    /**
     * Get the file name.
     *
     * @return The file name.
     */
    String getName();

    /**
     * Get the file path.
     *
     * @return The file path.
     */
    String getPath();

    /**
     * Get the file's MIME type.
     *
     * @return The file's MIME type.
     */
    String getMimeType();

    /**
     * Get the file's character encoding.
     *
     * @return The file's character encoding.
     */
    String getEncoding();

    /**
     * Get the file size.
     *
     * @return The file size.
     */
    long getSize();

    /**
     * Get the file's last modified date.
     *
     * @return The file's last modified date.
     */
    Calendar getLastModified();
}

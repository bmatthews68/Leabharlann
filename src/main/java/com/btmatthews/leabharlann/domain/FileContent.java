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

/**
 * Describes a file's content.
 *
 * @author <a href="mailto:brian@btmatthews.com">Brian Matthews</a>
 * @since 1.0.0
 */
public interface FileContent {

    /**
     * Get the workspace containing the file.
     *
     * @return The workspace.
     */
    String getWorkspace();

    /**
     * Get the file identifier.
     *
     * @return The file identifier.
     */
    String getId();
}
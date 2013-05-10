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

import com.btmatthews.leabharlann.domain.Workspace;

/**
 * An implementation of the {@link Workspace} descriptor.
 *
 * @author <a href="mailto:brian@btmatthews.com">Brian Matthews</a>
 * @since 1.0.0
 */
public class WorkspaceImpl implements Workspace {

    /**
     * The workspace name.
     */
    private String name;

    /**
     * Initialize the workspace descriptor.
     *
     * @param name The workspace name.
     */
    public WorkspaceImpl(final String name) {
        this.name = name;
    }

    /**
     * Get the workspace name.
     *
     * @return The workspace name.
     */
    public String getName() {
        return name;
    }
}

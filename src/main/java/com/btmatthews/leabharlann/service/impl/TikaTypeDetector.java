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

import com.btmatthews.leabharlann.service.TypeDetector;
import org.apache.tika.Tika;
import org.springframework.stereotype.Component;


/**
 * Detecting the content type of a file using Apache Tika.
 *
 * @author <a href="mailto:brian@btmatthews.com">Brian Matthews</a>
 * @since 1.0.0
 */
@Component
public class TikaTypeDetector implements TypeDetector {

    /**
     * The Apache Tika utility used to detect the content type.
     */
    private final Tika tika;

    /**
     * Initialize the content type detector injecting the Apache Tika utility.
     *
     * @param tika The Apache Tika utility.
     */
    public TikaTypeDetector(final Tika tika) {
        this.tika = tika;
    }

    /**
     * Detect the content type of a file.
     *
     * @param name     The name of the file.
     * @param contents The contents of the file.
     * @return The content (MIME) type.
     */
    @Override
    public String detect(final String name,
                         final byte[] contents) {
        return tika.detect(contents, name);
    }
}

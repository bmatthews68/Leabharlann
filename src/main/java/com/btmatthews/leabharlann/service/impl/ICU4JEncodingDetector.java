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

import com.btmatthews.leabharlann.service.EncodingDetector;
import com.ibm.icu.text.CharsetDetector;
import com.ibm.icu.text.CharsetMatch;
import org.springframework.stereotype.Component;

/**
 * Implementations are responsible for detecting the content encoding of file contents.
 *
 * @author <a href="mailto:brian@btmatthews.com">Brian Matthews</a>
 * @since 1.0.0
 */
@Component
public class ICU4JEncodingDetector implements EncodingDetector {

    @Override
    public String detect(final String name,
                         final byte[] contents) {
        if (contents == null || contents.length == 0) {
            return null;
        } else {
            final CharsetDetector detector = new CharsetDetector();
            detector.setText(contents);
            final CharsetMatch match = detector.detect();
            if (match == null) {
                return null;
            } else {
                return match.getName();
            }
        }
    }
}

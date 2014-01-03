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

package com.btmatthews.leabharlann.service;

import java.io.InputStream;

/**
 * Created with IntelliJ IDEA.
 * User: bmatthews68
 * Date: 13/05/2013
 * Time: 08:20
 * To change this template use File | Settings | File Templates.
 */
public interface ImportCallback {

    void directory(String path) throws Exception;

    void file(String path, long lastModified, byte[] contents) throws Exception;
}

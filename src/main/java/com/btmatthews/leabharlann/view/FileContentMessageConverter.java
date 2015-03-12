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

package com.btmatthews.leabharlann.view;

import com.btmatthews.atlas.jcr.JCRAccessor;
import com.btmatthews.atlas.jcr.NodeCallback;
import com.btmatthews.atlas.jcr.RepositoryAccessException;
import com.btmatthews.leabharlann.domain.FileContent;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.AbstractHttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.stereotype.Component;

import javax.jcr.Binary;
import javax.jcr.Node;
import javax.jcr.Property;
import javax.jcr.Session;
import java.io.IOException;
import java.util.Calendar;
import java.util.Collections;

/**
 * A Spring MVC message converter that is used to write a file's contents to the HTTP servlet response's output stream.
 *
 * @author <a href="mailto:brian@btmatthews.com">Brian Matthews</a>
 * @since 1.0.0
 */
@Component
public class FileContentMessageConverter extends AbstractHttpMessageConverter<FileContent> {

    /**
     * The {@link JCRAccessor} API object used to access the Java Content Repository.
     */
    private JCRAccessor jcrAccessor;

    /**
     * Inject the {@link JCRAccessor} API object used to access the Java Content Repository.
     *
     * @param accessor The {@link JCRAccessor} API object.
     */
    @Autowired
    public void setJcrAccessor(final JCRAccessor accessor) {
        jcrAccessor = accessor;
    }

    /**
     * Return true if this message converter supports the class {@code clazz}.
     *
     * @param clazz A class description.
     * @return Returns true {@code clazz} can be assigned to {@link FileContent}.
     */
    @Override
    protected boolean supports(final Class<?> clazz) {
        return FileContent.class.isAssignableFrom(clazz);
    }

    /**
     * Always throws {@link HttpMessageNotReadableException} to indicate that reading {@link FileContent} descriptors is
     * not supported.
     *
     * @param clazz        A class description.
     * @param inputMessage Used to access the servlet request headers and input stream.
     * @return Always throws an exception.
     * @throws HttpMessageNotReadableException Indicates that a {@link FileContent} cannot be read.
     */
    @Override
    protected FileContent readInternal(final Class<? extends FileContent> clazz, final HttpInputMessage inputMessage) throws HttpMessageNotReadableException {
        throw new HttpMessageNotReadableException("Cannot read messages of this type");
    }

    /**
     * Set the HTTP headers on the servlet response and stream the file contents to the servlet response's output stream.
     *
     * @param fileContent   Describes the file content.
     * @param outputMessage Used to access the servlet response headers and output stream.
     * @throws IOException                     If there was an error streaming the file content.
     * @throws HttpMessageNotWritableException If there was problem retrieving the file content from the Java Content Repository.
     */
    @Override
    protected void writeInternal(final FileContent fileContent, final HttpOutputMessage outputMessage) throws IOException, HttpMessageNotWritableException {
        try {
            jcrAccessor.withNodeId(fileContent.getWorkspace(), fileContent.getId(), new NodeCallback() {
                @Override
                public Object doInSessionWithNode(Session session, Node node) throws Exception {
                    final Node resourceNode = node.getNode(Node.JCR_CONTENT);
                    final String mimeType = jcrAccessor.getStringProperty(resourceNode, Property.JCR_MIMETYPE);
                    final Calendar lastModified = jcrAccessor.getCalendarProperty(resourceNode, Property.JCR_LAST_MODIFIED);
                    final Binary data = jcrAccessor.getBinaryProperty(resourceNode, Property.JCR_DATA);
                    if (jcrAccessor.hasProperty(resourceNode, Property.JCR_ENCODING)) {
                        final String encoding = jcrAccessor.getStringProperty(resourceNode, Property.JCR_ENCODING);
                        outputMessage.getHeaders().setContentType(new MediaType(MediaType.valueOf(mimeType), Collections.singletonMap("charset", encoding)));
                    } else {
                        outputMessage.getHeaders().setContentType(MediaType.valueOf(mimeType));
                    }
                    outputMessage.getHeaders().setContentLength(data.getSize());
                    if (lastModified != null) {
                        outputMessage.getHeaders().setLastModified(lastModified.getTimeInMillis());
                    }
                    outputMessage.getHeaders().set("Content-Disposition", "attachment;filename=" + node.getName());
                    IOUtils.copy(data.getStream(), outputMessage.getBody());
                    return null;
                }
            });
        } catch (final RepositoryAccessException e) {
            throw new HttpMessageNotWritableException(e.getLocalizedMessage());
        }
    }
}

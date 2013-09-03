package com.btmatthews.leabharlann.service.impl;

import com.btmatthews.leabharlann.service.ImportCallback;
import com.btmatthews.leabharlann.service.ImportSource;
import org.apache.commons.io.FileUtils;

import java.io.File;

/**
 * Created with IntelliJ IDEA.
 * User: bmatthews68
 * Date: 14/05/2013
 * Time: 08:34
 * To change this template use File | Settings | File Templates.
 */
public class FileImportSource implements ImportSource {

    private File file;

    public FileImportSource(final File file) {
        this.file = file;
    }

    @Override
    public void process(final ImportCallback callback) throws Exception {
        if (file.isDirectory()) {
            processDirectory(file, callback);
        } else {
            final byte[] data = FileUtils.readFileToByteArray(file);
            callback.file("/" + file.getName(), file.lastModified(), data);
        }
    }

    private void processDirectory(final File current, final ImportCallback callback) throws Exception {
        for (final File child : current.listFiles()) {
            final String path = "/" + file.toURI().relativize(child.toURI()).getPath();
            if (child.isDirectory()) {
                callback.directory(path);
                processDirectory(child, callback);
            } else {
                final byte[] data = FileUtils.readFileToByteArray(child);
                callback.file(path, child.lastModified(), data);
            }
        }
    }
}

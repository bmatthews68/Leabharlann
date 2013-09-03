package com.btmatthews.leabharlann.service.impl;

import com.btmatthews.leabharlann.service.ImportCallback;
import com.btmatthews.leabharlann.service.ImportSource;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * Created with IntelliJ IDEA.
 * User: bmatthews68
 * Date: 13/05/2013
 * Time: 08:24
 * To change this template use File | Settings | File Templates.
 */
public class ZipFileImportSource implements ImportSource {

    private ZipInputStream zipInputStream;

    public ZipFileImportSource(final String file) throws IOException {
        this(new File(file));
    }

    public ZipFileImportSource(final File file) throws IOException {
        this(new FileInputStream(file));
    }

    public ZipFileImportSource(final InputStream inputStream) {
        zipInputStream = new ZipInputStream(inputStream);
    }

    @Override
    public void process(final ImportCallback callback) throws Exception {

        for (ZipEntry entry = zipInputStream.getNextEntry(); entry != null; entry = zipInputStream.getNextEntry()) {
            final String path = entry.getName();
            if (path.endsWith("/")) {
                callback.directory(path);
            } else {
                final long size = entry.getSize();
                final long lastModified = entry.getTime();
                final byte[] contents = IOUtils.toByteArray(zipInputStream, size);
                callback.file(path, lastModified, contents);
            }
        }
    }
}

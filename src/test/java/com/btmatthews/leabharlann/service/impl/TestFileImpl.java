package com.btmatthews.leabharlann.service.impl;

import com.btmatthews.leabharlann.domain.File;
import org.junit.Before;
import org.junit.Test;

import java.util.Calendar;

import static org.junit.Assert.assertEquals;

/**
 * Created with IntelliJ IDEA.
 * User: bmatthews68
 * Date: 14/05/2013
 * Time: 20:36
 * To change this template use File | Settings | File Templates.
 */
public class TestFileImpl {

    private static final String ID = "beb0edc0-bccd-11e2-9e96-0800200c9a66";
    private static final String NAME = "index.html";
    private static final String PATH = "/";
    private static final String MIME_TYPE = "text/html";
    private static final String ENCODING = "ISO-8859-1";
    private static final long SIZE = 367;
    private static final Calendar LAST_MODIFIED = Calendar.getInstance();
    private File file;

    @Before
    public void setup() {
        file = new FileImpl(ID, NAME, PATH, MIME_TYPE, ENCODING, SIZE, LAST_MODIFIED);
    }

    @Test
    public void checkId() {
        assertEquals(ID, file.getId());
    }

    @Test
    public void checkName() {
        assertEquals(NAME, file.getName());
    }

    @Test
    public void checkPath() {
        assertEquals(PATH, file.getPath());
    }

    @Test
    public void checkMimeType() {
        assertEquals(MIME_TYPE, file.getMimeType());
    }

    @Test
    public void checkEncoding() {
        assertEquals(ENCODING, file.getEncoding());
    }

    @Test
    public void checkSize() {
        assertEquals(SIZE, file.getSize());
    }

    @Test
    public void checkLastModified() {
        assertEquals(LAST_MODIFIED, file.getLastModified());
    }
}

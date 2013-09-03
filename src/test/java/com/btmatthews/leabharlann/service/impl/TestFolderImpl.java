package com.btmatthews.leabharlann.service.impl;

import com.btmatthews.leabharlann.domain.Folder;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertNull;
import static org.junit.Assert.assertEquals;

/**
 * Created with IntelliJ IDEA.
 * User: bmatthews68
 * Date: 14/05/2013
 * Time: 20:48
 * To change this template use File | Settings | File Templates.
 */
public class TestFolderImpl {

    private static final String ID = "60487670-bccf-11e2-9e96-0800200c9a66";
    private static final String NAME = "/";
    private static final String PATH = "/";
    private Folder folder;

    @Before
    public void setup() {
        folder = new FolderImpl(ID, NAME, PATH);
    }

    @Test
    public void checkId() {
        assertEquals(ID, folder.getId());
    }

    @Test
    public void checkName() {
        assertEquals(NAME, folder.getName());
    }

    @Test
    public void checkPath() {
        assertEquals(PATH, folder.getPath());
    }

    @Test
    public void checkAttributes() {
        assertNull(folder.getAttributes());
    }
}

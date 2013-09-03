package com.btmatthews.leabharlann.service.impl;

import com.btmatthews.leabharlann.domain.FileContent;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created with IntelliJ IDEA.
 * User: bmatthews68
 * Date: 14/05/2013
 * Time: 20:56
 * To change this template use File | Settings | File Templates.
 */
public class TestFileContentImpl {
    private static final String WORKSPACE = "default";
    private static final String ID = "62174d90-bcd0-11e2-9e96-0800200c9a66";

    private FileContent fileContent;

    @Before
    public void setup() {
        fileContent = new FileContentImpl(WORKSPACE, ID);
    }

    @Test
    public void checkWorkspace() {
        assertEquals(WORKSPACE, fileContent.getWorkspace());
    }

    @Test
    public void checkId() {
        assertEquals(ID, fileContent.getId());
    }
}

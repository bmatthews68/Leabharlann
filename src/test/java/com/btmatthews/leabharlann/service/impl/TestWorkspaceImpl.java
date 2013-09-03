package com.btmatthews.leabharlann.service.impl;

import com.btmatthews.leabharlann.domain.Workspace;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created with IntelliJ IDEA.
 * User: bmatthews68
 * Date: 14/05/2013
 * Time: 20:53
 * To change this template use File | Settings | File Templates.
 */
public class TestWorkspaceImpl {

    private static final String NAME = "default";
    private Workspace workspace;

    @Before
    public void setup() {
        workspace = new WorkspaceImpl(NAME);
    }

    @Test
    public void checkName() {
        assertEquals(NAME, workspace.getName());
    }
}

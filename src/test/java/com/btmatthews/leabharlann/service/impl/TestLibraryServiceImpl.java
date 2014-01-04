package com.btmatthews.leabharlann.service.impl;

import com.btmatthews.atlas.jcr.JCRAccessor;
import com.btmatthews.atlas.jcr.NodeCallback;
import com.btmatthews.atlas.jcr.SessionCallback;
import com.btmatthews.leabharlann.domain.Folder;
import com.btmatthews.leabharlann.domain.Workspace;
import com.btmatthews.leabharlann.service.EncodingDetector;
import com.btmatthews.leabharlann.service.LibraryService;
import com.btmatthews.leabharlann.service.TypeDetector;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import javax.jcr.Node;
import javax.jcr.Session;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

/**
 * Created with IntelliJ IDEA.
 * User: bmatthews68
 * Date: 14/05/2013
 * Time: 23:05
 * To change this template use File | Settings | File Templates.
 */
public class TestLibraryServiceImpl {

    @Mock
    private JCRAccessor jcrAccessor;
    @Mock
    private Session jcrSession;
    @Mock
    private javax.jcr.Workspace jcrWorkspace;
    @Mock
    private Node jcrFolderNode;
    @Mock
    private Node jcrRootNode;
    @Mock
    private EncodingDetector encodingDetector;
    @Mock
    private TypeDetector typeDetector;
    @Mock
    private Workspace workspace;
    @Mock
    private Folder rootFolder;
    @Mock
    private Folder folder;
    private LibraryService libraryService;

    @Before
    public void setup() {
        initMocks(this);
        when(workspace.getName()).thenReturn("default");
        libraryService = new LibraryServiceImpl(
                jcrAccessor,
                typeDetector,
                encodingDetector);
    }

    @Test
    public void verifyGetWorkspaces() throws Exception {
        when(jcrAccessor.withSession(anyString(), any(SessionCallback.class))).thenAnswer(new Answer() {
            @Override
            public Object answer(final InvocationOnMock invocation) throws Throwable {
                when(jcrSession.getWorkspace()).thenReturn(jcrWorkspace);
                when(jcrWorkspace.getAccessibleWorkspaceNames()).thenReturn(new String[]{"default", "wip"});
                return ((SessionCallback<List<Workspace>>) invocation.getArguments()[1]).doInSession(jcrSession);
            }
        });
        final List<Workspace> workspaces = libraryService.getWorkspaces();
        assertNotNull(workspaces);
        assertEquals(2, workspaces.size());
        assertEquals("default", workspaces.get(0).getName());
        assertEquals("wip", workspaces.get(1).getName());
        verify(jcrSession).getWorkspace();
        verify(jcrWorkspace).getAccessibleWorkspaceNames();
        verify(jcrAccessor).withSession(eq("default"), any(SessionCallback.class));
        verifyNoMoreInteractions(jcrAccessor, jcrSession, jcrWorkspace);
    }

    @Test
    public void verifyGetWorkspace() {
        final Workspace workspace = libraryService.getWorkspace("default");
        assertNotNull(workspace);
        assertEquals("default", workspace.getName());
    }

    @Test
    public void verifyGetRoot() throws Exception {
        when(jcrAccessor.withRoot(anyString(), any(NodeCallback.class))).thenAnswer(new Answer<Object>() {
            @Override
            public Object answer(final InvocationOnMock invocation) throws Throwable {
                when(jcrRootNode.getIdentifier()).thenReturn("e7a74180-bcef-11e2-9e96-0800200c9a66");
                when(jcrRootNode.getName()).thenReturn("");
                when(jcrRootNode.getPath()).thenReturn("/");
                return ((NodeCallback<Folder>) invocation.getArguments()[1]).doInSessionWithNode(jcrSession, jcrRootNode);
            }
        });
        final Folder folder = libraryService.getRoot(workspace);
        assertNotNull(folder);
        assertEquals("e7a74180-bcef-11e2-9e96-0800200c9a66", folder.getId());
        assertEquals("", folder.getName());
        assertEquals("/", folder.getPath());
        assertNull(folder.getAttributes());
        verify(workspace).getName();
        verify(jcrRootNode).getIdentifier();
        verify(jcrRootNode).getName();
        verify(jcrRootNode).getPath();
        verify(jcrAccessor).withRoot(eq("default"), any(NodeCallback.class));
        verifyNoMoreInteractions(jcrAccessor, jcrSession, jcrRootNode);
    }

    @Test
    public void verifyGetFolder() throws Exception {
        when(jcrAccessor.withNodeId(anyString(), anyString(), any(NodeCallback.class))).thenAnswer(new Answer() {
            @Override
            public Object answer(final InvocationOnMock invocation) throws Throwable {
                when(jcrFolderNode.getIdentifier()).thenReturn("d1d48070-bcea-11e2-9e96-0800200c9a66");
                when(jcrFolderNode.getName()).thenReturn("css");
                when(jcrFolderNode.getPath()).thenReturn("/documents");
                return ((NodeCallback<Folder>) invocation.getArguments()[2]).doInSessionWithNode(jcrSession, jcrFolderNode);
            }
        });

        final Folder folder = libraryService.getFolder(workspace, "d1d48070-bcea-11e2-9e96-0800200c9a66");
        assertNotNull(folder);
        assertEquals("d1d48070-bcea-11e2-9e96-0800200c9a66", folder.getId());
        assertEquals("css", folder.getName());
        assertEquals("/documents", folder.getPath());
        assertNull(folder.getAttributes());
        verify(workspace).getName();
        verify(jcrFolderNode).getIdentifier();
        verify(jcrFolderNode).getName();
        verify(jcrFolderNode).getPath();
        verify(jcrAccessor).withNodeId(eq("default"), eq("d1d48070-bcea-11e2-9e96-0800200c9a66"), any(NodeCallback.class));
        verifyNoMoreInteractions(jcrAccessor, jcrSession, jcrFolderNode);
    }
}

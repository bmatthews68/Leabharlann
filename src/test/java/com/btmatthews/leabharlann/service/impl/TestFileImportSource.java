package com.btmatthews.leabharlann.service.impl;

import com.btmatthews.leabharlann.service.ImportCallback;
import com.btmatthews.leabharlann.service.ImportSource;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mock;

import java.io.File;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.MockitoAnnotations.initMocks;

/**
 * Created with IntelliJ IDEA.
 * User: bmatthews68
 * Date: 14/05/2013
 * Time: 20:16
 * To change this template use File | Settings | File Templates.
 */
public class TestFileImportSource {

    @Mock
    private ImportCallback callback;

    @Before
    public void setup() {
        initMocks(this);
    }

    @Test
    public void importFile() throws Exception {
        final File file = new File("target/test-classes/documents/index.html");
        final ImportSource source = new FileImportSource(file);
        source.process(callback);
        verify(callback).file(eq("/index.html"), anyLong(), any(byte[].class));
        verifyNoMoreInteractions(callback);
    }

    @Test
    public void importDirectory() throws Exception {
        final File file = new File("target/test-classes/documents");
        final ImportSource source = new FileImportSource(file);
        source.process(callback);
        verify(callback).directory(eq("/css/"));
        verify(callback).directory(eq("/images/"));
        verify(callback).file(eq("/index.html"), anyLong(), any(byte[].class));
        verify(callback).file(eq("/css/style.css"), anyLong(), any(byte[].class));
        verify(callback).file(eq("/images/background.png"), anyLong(), any(byte[].class));
        verifyNoMoreInteractions(callback);
    }
}

package com.btmatthews.leabharlann.view;

import com.btmatthews.atlas.jcr.JCRAccessor;
import com.btmatthews.leabharlann.domain.FileContent;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;
import org.mockito.Mock;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.MockitoAnnotations.initMocks;

/**
 * Created with IntelliJ IDEA.
 * User: bmatthews68
 * Date: 15/05/2013
 * Time: 08:30
 * To change this template use File | Settings | File Templates.
 */
public class TestFileContentMessageConverter {

    @Rule
    public ErrorCollector collector = new ErrorCollector();
    private HttpMessageConverter converter;
    @Mock
    private JCRAccessor jcrAccessor;

    @Before
    public void setup() {
        initMocks(this);
        final FileContentMessageConverter converterImpl = new FileContentMessageConverter();
        converterImpl.setJcrAccessor(jcrAccessor);
        converterImpl.setSupportedMediaTypes(Arrays.asList(MediaType.ALL));
        converter = converterImpl;
    }

    @Test
    public void canReadSupportedClassesOnly() {
        collector.checkThat(converter.canRead(Object.class, null), is(false));
        collector.checkThat(converter.canRead(FileContent.class, null), is(true));
        collector.checkThat(converter.canRead(FileContent.class, MediaType.APPLICATION_FORM_URLENCODED), is(true));
    }

    @Test
    public void x() {
        collector.checkThat(converter.canWrite(Object.class, null), is(false));
        collector.checkThat(converter.canRead(FileContent.class, null), is(true));
        collector.checkThat(converter.canRead(FileContent.class, MediaType.IMAGE_JPEG), is(true));
    }

    @Test
    public void thereShouldBeNoSpecificMediaTypesSupported() {
        final List<MediaType> mediaTypes = converter.getSupportedMediaTypes();
        collector.checkThat(mediaTypes, is(notNullValue()));
        collector.checkThat(mediaTypes, hasItem(MediaType.ALL));
    }
 }
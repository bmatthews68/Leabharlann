package com.btmatthews.leabharlann.service.impl;

import com.btmatthews.leabharlann.service.EncodingDetector;
import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * Created with IntelliJ IDEA.
 * User: bmatthews68
 * Date: 14/05/2013
 * Time: 20:59
 * To change this template use File | Settings | File Templates.
 */
public class TestICU4JEncodingDetector {

    private static final byte[] NOISE = {-25, -54, -27, 93, 3, -4, 55, 13, -114, -58, 119, 52, -116, 65, -42, 116};
    private EncodingDetector detector;

    @Before
    public void setup() {
        detector = new ICU4JEncodingDetector();
    }

    @Test
    public void test() throws IOException {
        final File file = new File("target/test-classes/documents/index.html");
        final byte[] data = FileUtils.readFileToByteArray(file);
        final String encoding = detector.detect("index.html", data);
        assertEquals("ISO-8859-1", encoding);
    }

    @Test
    public void test1() throws IOException {
        final String encoding = detector.detect("style.css", new byte[0]);
        assertNull(encoding);
    }

    @Test
    public void test3() {
        final String encoding = detector.detect("dummy.txt", NOISE);
        assertNull(encoding);
    }

    @Test
    public void test4() {
        final String encoding = detector.detect(null, null);
        assertNull(encoding);
    }
}

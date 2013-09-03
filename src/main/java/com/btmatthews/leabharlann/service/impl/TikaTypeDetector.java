package com.btmatthews.leabharlann.service.impl;

import com.btmatthews.leabharlann.service.TypeDetector;
import org.apache.tika.Tika;

/**
 * Created with IntelliJ IDEA.
 * User: bmatthews68
 * Date: 13/05/2013
 * Time: 21:42
 * To change this template use File | Settings | File Templates.
 */
public class TikaTypeDetector implements TypeDetector {

    private Tika tika;

    public void setTika(final Tika tika) {
        this.tika = tika;
    }

    @Override
    public String detect(String name, byte[] contents) {
        return tika.detect(contents, name);
    }
}

package com.btmatthews.leabharlann.service;

import java.io.InputStream;

/**
 * Created with IntelliJ IDEA.
 * User: bmatthews68
 * Date: 13/05/2013
 * Time: 08:20
 * To change this template use File | Settings | File Templates.
 */
public interface ImportCallback {

    void directory(String path) throws Exception;

    void file(String path, long lastModified, byte[] contents) throws Exception;
}

package com.btmatthews.leabharlann.service;

/**
 * Created with IntelliJ IDEA.
 * User: bmatthews68
 * Date: 13/05/2013
 * Time: 21:36
 * To change this template use File | Settings | File Templates.
 */
public interface TypeDetector {

    String detect(String name, byte[] contents);
}

package com.btmatthews.leabharlann.service;

/**
 * Created with IntelliJ IDEA.
 * User: bmatthews68
 * Date: 13/05/2013
 * Time: 21:47
 * To change this template use File | Settings | File Templates.
 */
public interface EncodingDetector {

    String detect(String name, byte[] data);
}

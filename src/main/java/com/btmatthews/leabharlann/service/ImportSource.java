package com.btmatthews.leabharlann.service;

/**
 * Created with IntelliJ IDEA.
 * User: bmatthews68
 * Date: 13/05/2013
 * Time: 08:19
 * To change this template use File | Settings | File Templates.
 */
public interface ImportSource {

    void process(ImportCallback callback) throws Exception;
}

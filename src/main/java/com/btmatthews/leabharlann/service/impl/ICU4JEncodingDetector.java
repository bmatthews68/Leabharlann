package com.btmatthews.leabharlann.service.impl;

import com.btmatthews.leabharlann.service.EncodingDetector;
import com.ibm.icu.text.CharsetDetector;
import com.ibm.icu.text.CharsetMatch;

/**
 * Created with IntelliJ IDEA.
 * User: bmatthews68
 * Date: 13/05/2013
 * Time: 21:48
 * To change this template use File | Settings | File Templates.
 */
public class ICU4JEncodingDetector implements EncodingDetector {

    @Override
    public String detect(final String name, final byte[] contents) {
        if (contents == null || contents.length == 0) {
            return null;
        } else {
            final CharsetDetector detector = new CharsetDetector();
            detector.setText(contents);
            final CharsetMatch match = detector.detect();
            if (match == null) {
                return null;
            } else {
                return match.getName();
            }
        }
    }
}

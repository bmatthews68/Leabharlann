package com.btmatthews.leabharlann.domain;

import java.util.Calendar;

public interface File {

	String getId();

	String getName();

	String getPath();

	String getMimeType();

	String getEncoding();

	Calendar getLastModified();
}

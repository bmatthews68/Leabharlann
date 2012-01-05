package com.btmatthews.leabharlann.service;

import java.io.InputStream;

import com.btmatthews.leabharlann.domain.File;

public interface FileDataCallback {

	void visitFileData(File file, InputStream dataStream) throws Exception;
}

package com.btmatthews.leabharlann.service;

import com.btmatthews.leabharlann.domain.File;

public interface FileCallback {

	void visit(File file);
}

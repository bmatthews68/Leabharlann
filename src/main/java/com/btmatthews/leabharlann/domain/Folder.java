package com.btmatthews.leabharlann.domain;

import java.util.Map;

public interface Folder {

	String getId();
	
	String getName();
	
	String getPath();
	
	Map<String, String> getAttributes();
}

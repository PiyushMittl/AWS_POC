package com.piyush.poc.texttomp3;

import java.io.IOException;
import java.io.InputStream;

public class ResourceHelper {

	public static InputStream getResourceAsIS(String fileName) throws IOException {
		return new ResourceHelper().getFile(fileName);
	}

	private InputStream getFile(String fileName) throws IOException {
		// Get file from resources folder
		ClassLoader classLoader = getClass().getClassLoader();
		return classLoader.getResourceAsStream(fileName);
	}

}
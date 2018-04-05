package com.github.wouterdek.jrenderdoc;

import java.io.*;

public class Utilities {
	private Utilities() { }

	public static File extractResourceToTempFile(String resourcePath, String prefix, String suffix) throws IOException {
		try(InputStream in = getResourceStream(resourcePath)){
			File tempFile = File.createTempFile(prefix, suffix);
			try(OutputStream out = new FileOutputStream(tempFile)){
				byte[] buffer = new byte[8192];
	            int read;
	            while ((read = in.read(buffer, 0, buffer.length)) >= 0) {
	                out.write(buffer, 0, read);
	            }
			}
			return tempFile;
		}
	}

	public static InputStream getResourceStream(String resourcePath){
        return Utilities.class.getResourceAsStream(resourcePath);
    }
}
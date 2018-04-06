package com.github.wouterdek.jrenderdoc;

public class Capture {
	private String filename;
	private long unixTime;

	Capture(String filename, long unixTime){
		this.filename = filename;
		this.unixTime = unixTime;
	}

	public String getFilename(){
		return filename;
	}

	public long getUnixTimestamp(){
		return unixTime;
	}
}
package com.github.wouterdek.jrenderdoc;

enum Architecture {
	X86, X64;

	public static Architecture getSystemArchitecture(){
		String arch = System.getProperty("os.arch");
		switch(arch){
			case "x86":
			case "x86_32":
				return X86;
			case "amd64":
			case "x86_64":
				return X64;
			default:
				return null;
		}
	}
}
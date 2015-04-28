package com.vnetpublishing.java.suapp;

import java.io.File;
import java.io.IOException;

public class WinSuperUserDetector 
	implements ISuperUserDetector
{

	public boolean isSuperUser() 
	{
		final String programfiles = System.getenv("PROGRAMFILES");
		
		if (null == programfiles || programfiles.length() < 1) {
			throw new IllegalStateException("OS mismatch. Program Files directory not detected");
		}
		
		File testPriv = new File(programfiles);
		if (!testPriv.canWrite()) {
			return false;
		}
		File fileTest = null;
		
		try {
			fileTest = File.createTempFile("testsu", ".dll", testPriv);
		} catch (IOException e) {
			return false;
		} finally {
			if (fileTest != null) {
				fileTest.delete();
			}
		}
		return true;
	}
	
}

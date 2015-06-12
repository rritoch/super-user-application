package com.vnetpublishing.java.suapp.posix;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import com.vnetpublishing.java.suapp.ISuperUserDetector;

public class PosixSuperUserDetector
	implements ISuperUserDetector 
{

	private static final String ID_CMD = "/usr/bin/id";
	
	public boolean isSuperUser() 
	{
		int gid = Short.MAX_VALUE;
		try {
			Process p = Runtime.getRuntime().exec(new String[] { ID_CMD, "-g" });
			BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));
			gid = Integer.parseInt(r.readLine());
		} catch (IOException ex) {
			throw new IllegalStateException("ID command failed",ex);
		} catch (NumberFormatException ex) {
			throw new IllegalStateException("ID command returned unexpected value",ex);
		} catch (SecurityException ex) {
			throw new IllegalStateException("ID command not accessible", ex);
		}
		
		return gid == 0;
	}
}

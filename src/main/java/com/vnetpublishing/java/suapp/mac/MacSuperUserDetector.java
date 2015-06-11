package com.vnetpublishing.java.suapp.mac;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;

import com.vnetpublishing.java.suapp.ISuperUserDetector;

public class MacSuperUserDetector implements ISuperUserDetector
{
	private static final String ID_CMD = "/usr/bin/id";
	
	public boolean isSuperUser() 
	{
		List<String> group_ids = null;
		
		try {
			Process p = Runtime.getRuntime().exec(new String[] { ID_CMD, "-G" });
			BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));
			group_ids = Arrays.asList(r.readLine().split("\\s+"));
		} catch (IOException ex) {
			throw new IllegalStateException("ID command failed",ex);
		} catch (NumberFormatException ex) {
			throw new IllegalStateException("ID command returned unexpected value",ex);
		} catch (SecurityException ex) {
			throw new IllegalStateException("ID command not accessible", ex);
		}
		
		return group_ids.contains("0");
	}
}

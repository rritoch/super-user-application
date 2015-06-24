package com.vnetpublishing.java.suapp;

public class SU {

	private static boolean daemon = false;
	public static boolean prefer_stdio = false;
	public static boolean debug = false;
	
	public static int run(ISuperUserApplication app, String[] args) 
	{
		int result = -1;
		
		if (app.isSuperUser()) {
			result = app.run(args);
		} else {
			result = app.sudo(args);
		}
		
		if (!daemon) {
			System.exit(result);
		}
		
		return result;
	}
	
	public static void run(ISuperUserApplication app) 
	{
		run(app,new String[]{});
	}
	
	public static String getOS() 
	{
		String os_name = System.getProperty("os.name");
		String parts[];
		if (null != os_name) {
			parts = os_name.trim().toLowerCase().split("\\s+");
		} else {
			parts = new String[]{};
		}

		if (parts.length < 1) {
			return null;
		}
		return parts[0];
	}

	public static void setDaemon(boolean b) {
		daemon = b;
	}
	
}

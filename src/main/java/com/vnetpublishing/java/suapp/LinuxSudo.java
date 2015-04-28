package com.vnetpublishing.java.suapp;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;

public class LinuxSudo implements ISudo {

    public static int executeAsAdministrator(String command, String[] args)
    {
    	
    	int len = args == null ? 0 : args.length;
    	
    	ArrayList<String> pargs = new ArrayList(len+2);
    	
    	pargs.add("/usr/bin/sudo");
    	pargs.add(command);
    	
    	for(int idx=0;idx<len;idx++) {
    		pargs.add(args[idx]);
    	}
    	
    	try {
    		ProcessBuilder builder = new ProcessBuilder(pargs);
    		
    		builder.redirectOutput(ProcessBuilder.Redirect.INHERIT);
    		
    		Process p1 = builder.start();
			return p1.waitFor();
		} catch (IOException ex) {
			throw new IllegalStateException(ex);
		} catch (InterruptedException ex) {
			throw new IllegalStateException(ex);
		}
    }
    
	public int sudo(String[] args) {
		try {
			String jarPath = LinuxSudo.class.getProtectionDomain().getCodeSource().getLocation().getPath();
        	String decodedPath = URLDecoder.decode(jarPath, "UTF-8");
        
        	ArrayList<String> pargs = new ArrayList(args.length + 2);
        
        	if (decodedPath.endsWith(".jar")) {
        		pargs.add("-jar");
            	pargs.add(decodedPath);
        	} else {
        		throw new IllegalStateException("Unable to perform elevation outside jar");
        	}
        
        	if (args != null) {
        		for(int idx=0;idx<args.length;idx++) {
        			pargs.add(args[idx]);
        		}
        	}
        	String[] sargs = pargs.toArray(new String[args.length + 2]);
        	
        	return executeAsAdministrator(System.getProperty("java.home") + "/bin/java", sargs);
        } catch (UnsupportedEncodingException ex) {
        	throw new RuntimeException(ex);
        }
	}
	
	
	public int sudo() 
	{
		return sudo(new String[]{});
	}

}

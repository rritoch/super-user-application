package com.vnetpublishing.java.suapp;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.management.ManagementFactory;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

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
    		
    		p1.waitFor();
    		
			return p1.exitValue();
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
        
        	String jcmd = System.getProperty("sun.java.command");
        	
        	
			if (jcmd == null || jcmd.length() < 1) {
				if (decodedPath.endsWith(".jar")) {
					pargs.add("-jar");
					pargs.add(decodedPath);
					
					if (args != null) {
						for(int idx=0;idx<args.length;idx++) {
							pargs.add(args[idx]);
						}
					}
				} else {
					throw new IllegalStateException("Unable to perform elevation outside of jar");
				}
			} else {
				List<String> inputArguments = ManagementFactory.getRuntimeMXBean().getInputArguments();
				
				Iterator<String> iap = inputArguments.iterator();
				while(iap.hasNext()) {
					pargs.add(iap.next());
				}
				
				String[] cmd = jcmd.split("\\s+");
				
				if (cmd.length > 0 && cmd[0].endsWith(".jar")) {
					pargs.add("-jar");
				}
				
				for(int idx=0;idx<cmd.length;idx++) {
					pargs.add(cmd[idx]);
				}
			}
			
			//System.out.println(pargs);
        	
        	String[] sargs = pargs.toArray(new String[pargs.size()]);
        	
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

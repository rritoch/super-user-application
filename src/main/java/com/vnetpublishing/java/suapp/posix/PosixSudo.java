package com.vnetpublishing.java.suapp.posix;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.management.ManagementFactory;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.vnetpublishing.java.suapp.ISudo;

public class PosixSudo implements ISudo {

	public static String argsToString(List args) {
		StringBuilder sb = new StringBuilder();
		Iterator<String> i = args.iterator();
		while(i.hasNext()) {
			sb.append(i.next());
			if (i.hasNext()) {
				sb.append(" ");
			}
		}
		return sb.toString();
	}
	public static void applySudoArgs(List args) 
	{
		File s;
		List holdargs = new ArrayList();
		holdargs.addAll(args);
		args.clear();
		
		
		// Try gksudo
		s = new File("/usr/bin/gksudo");
		if (s.canExecute()) {
			args.add("/usr/bin/gksudo");
			
			args.add("--description");
			args.add("Java Application");
			
			args.add(argsToString(holdargs));
			return;
		}
		
		// Try kdesudo
		s = new File("/usr/bin/kdesudo");
		if (s.canExecute()) {
			args.add("/usr/bin/kdesudo");
			args.add("-d");
			args.add("-c");
			
			//args.add("-c");
			args.add(argsToString(holdargs));
			
			args.add("--comment");
			args.add("Java application needs administrative privileges. Please enter your password");
			return;
		}
		
		// Try sudo
		s = new File("/usr/bin/sudo");
		if (s.canExecute()) {
			args.add("/usr/bin/sudo");
			args.addAll(holdargs);
			return;
		}
		
		throw new IllegalStateException("SUDO application not found!");
	}
	
    public static int executeAsAdministrator(String command, String[] args)
    {
    	
    	int len = args == null ? 0 : args.length;
    	
    	ArrayList<String> pargs = new ArrayList(len+2);
    	
    	pargs.add(command);
    	
    	for(int idx=0;idx<len;idx++) {
    		pargs.add(args[idx]);
    	}
    	
    	applySudoArgs(pargs);
    	
    	System.out.println(pargs);
    	try {
    		ProcessBuilder builder = new ProcessBuilder(pargs);
    		
    		builder.inheritIO();
    		
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
			String jarPath = PosixSudo.class.getProtectionDomain().getCodeSource().getLocation().getPath();
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

package com.vnetpublishing.java.suapp.mac;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.management.ManagementFactory;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.vnetpublishing.java.suapp.ISudo;

public class MacSudo implements ISudo {

	public static String escapeParam(String param) 
	{
		String parts[] = param.split("\\s+");
		if (parts.length < 2) {
			return param;
		}
		
		return String.format(
			"\"%s\"",
			param.replaceAll("\\", "\\\\\\\\").replaceAll("\"", "\\\\\\\"")
		);
	}
	
	private static String toParams(String[] params) 
	{
		StringBuilder sb = new StringBuilder();
		if (params.length > 0) {
			sb.append(params[0]);
		}
		for(int idx=1;idx<params.length;idx++) {
			sb.append(" ");
			sb.append(params[idx]);
		}
		return sb.toString();
	}
	
    public static int executeAsAdministrator(String command, String[] args)
    {
    	
    	int len = args == null ? 0 : args.length;
    	
    	List<String> cargs = new ArrayList<String>(args.length+1);
    	
    	cargs.add(command);
    	for(int idx=0;idx<len;idx++) {
    		cargs.add(args[idx]);
    	}
    	
    	ArrayList<String> pargs = new ArrayList<String>(3);
    	
    	pargs.add("/usr/bin/osascript");
    	pargs.add("-e");
    	pargs.add(String.format("do shell script \"%s\" with administrator privileges",toParams(cargs.toArray(new String[cargs.size()]))));
    	
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
			String jarPath = MacSudo.class.getProtectionDomain().getCodeSource().getLocation().getPath();
        	String decodedPath = URLDecoder.decode(jarPath, "UTF-8");
        
        	ArrayList<String> pargs = new ArrayList<String>(args.length + 2);
        
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

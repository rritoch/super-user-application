package com.vnetpublishing.java.suapp;

import java.io.UnsupportedEncodingException;
import java.lang.management.ManagementFactory;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import com.sun.jna.WString;
import com.sun.jna.platform.win32.Kernel32;
import com.sun.jna.platform.win32.Kernel32Util;


public class WinSudo implements ISudo 
{
	
	private static String escapeParam(String param) 
	{
		String parts[] = param.split("\\s+");
		if (parts.length < 2) {
			return param;
		}
		
		return String.format(
			"\"%s\"",
			param.replaceAll("\\", "\\\\").replaceAll("\"", "\\\"")
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
	
    public static int executeAsAdministrator(String command, String args)
    {
    	int lastError = 0;
        Shell32X.SHELLEXECUTEINFO execInfo = new Shell32X.SHELLEXECUTEINFO();
        execInfo.lpFile = new WString(command);
        if (args != null)
            execInfo.lpParameters = new WString(args);
        //execInfo.nShow = Shell32X.SW_SHOWDEFAULT;
        execInfo.nShow = Shell32X.SW_HIDE;
        
        execInfo.fMask = Shell32X.SEE_MASK_NOCLOSEPROCESS;
        execInfo.lpVerb = new WString("runas");
        boolean result = Shell32X.INSTANCE.ShellExecuteEx(execInfo);

        if (!result)
        {
            lastError = Kernel32.INSTANCE.GetLastError();
            String errorMessage = Kernel32Util.formatMessageFromLastErrorCode(lastError);
            throw new RuntimeException("Error performing elevation: " + lastError + ": " + errorMessage + " (apperror=" + execInfo.hInstApp + ")");
        }
        
        return lastError;
    }
	
	
	public int sudo(String[] args) {
		try {
			String jarPath = WinSudo.class.getProtectionDomain().getCodeSource().getLocation().getPath();
        	String decodedPath = URLDecoder.decode(jarPath, "UTF-8");
        	
        	decodedPath = decodedPath.substring(1, decodedPath.length());
        
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
        	
        	String strparams = toParams(pargs.toArray(new String[pargs.size()]));
        	
        	return executeAsAdministrator(System.getProperty("java.home") + "\\bin\\java", strparams);
        } catch (UnsupportedEncodingException ex) {
        	throw new RuntimeException(ex);
        }
	}
	
	public int sudo() 
	{
		return sudo(new String[]{});
	}

}

package com.vnetpublishing.java.suapp;

import java.io.UnsupportedEncodingException;
import java.lang.management.ManagementFactory;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import com.sun.jna.IntegerType;
import com.sun.jna.Native;
import com.sun.jna.WString;
import com.sun.jna.platform.win32.Kernel32;
import com.sun.jna.platform.win32.Kernel32Util;
import com.sun.jna.platform.win32.WinBase.PROCESS_INFORMATION;
import com.sun.jna.platform.win32.WinBase.STARTUPINFO;
import com.sun.jna.platform.win32.WinNT.HANDLE;
import com.sun.jna.ptr.IntByReference;


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
        
        // Setup Job
        
        PROCESS_INFORMATION pi = new PROCESS_INFORMATION();
        STARTUPINFO si = new STARTUPINFO(); 
        
        Kernel32X.JOBOBJECT_EXTENDED_LIMIT_INFORMATION jeli = new Kernel32X.JOBOBJECT_EXTENDED_LIMIT_INFORMATION();
        
        HANDLE hJob = Kernel32X.INSTANCE.CreateJobObject(null,null);
        
        jeli.BasicLimitInformation.limitFlags = Kernel32X.JOB_OBJECT_EXTENDED_LIMIT_KILL_ON_JOB_CLOSE;
        
        Kernel32X.INSTANCE.SetInformationJobObject(hJob, 2, jeli.getPointer(), jeli.size());
        
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
        
        
        Kernel32X.INSTANCE.AssignProcessToJobObject(hJob, execInfo.hProcess);
        
        IntByReference code = new IntByReference();
        Kernel32.INSTANCE.WaitForSingleObject(execInfo.hProcess, Kernel32.INFINITE);
        
        
        
        Kernel32.INSTANCE.GetExitCodeProcess(execInfo.hProcess, code);
        lastError = code.getValue();
        
        Kernel32X.INSTANCE.CloseHandle(execInfo.hProcess);
        Kernel32X.INSTANCE.CloseHandle(hJob);
        
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

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
import com.vnetpublishing.java.suapp.SU;

public class PosixSudo implements ISudo {

	public static List<String> stringToArgs(String in) 
	{
		int ptr = 0;
		int len = in.length();

		Character quote = null;
		boolean escape = false;
		List<String> ret = new ArrayList<String>();
		StringBuilder sb = new StringBuilder();
		boolean skipspaces = true;
		boolean dirty = false;
		while(ptr < len) {
			char c = in.charAt(ptr);
			if (quote == null) {
				switch (c) {
					case ' ':
					case '\r':
					case '\n':
					case '\t':
						if (!skipspaces) {
							if (dirty) {
								ret.add(sb.toString());
							}
							dirty = false;
							sb = new StringBuilder();
							skipspaces = true;
						}
						break;
					case '\'':
						quote = '\'';
						skipspaces = false;
						dirty = true;
						break;
					case '"':
						dirty = true;
						quote = '"';
						skipspaces = false;
						break;
					default:
						dirty = true;
						sb.append(c);
						skipspaces= false;
						break;
				}
			} else {
				if (escape) {
					switch(c) {
				
						case 'a':
							sb.append(Character.toChars(7)[0]);
							break;
						case 'b':
							sb.append('\b');
							break;
						case 'f':
							sb.append('\f');
							break;
						case 'n':
							sb.append('\n');
							break;
						case 'r':
							sb.append('\r');
							break;
						case 't':
							sb.append('\t');
							break;
						case 'v':
							sb.append(Character.toChars(11)[0]);
							break;
							// Octal and Hex characters not supported!
						case '0':
						case '1':
						case '2':
						case '3':
						case '4':
						case '5':
						case '6':
						case '7':
						case '8':
						case '9':
						case 'x':
							throw new IllegalArgumentException();
						default:
							sb.append(c);
					}
					escape = false;
				} else {
					if (c == '\\') {
						escape = true;
					} else if (c == quote) {
						quote = null;
					} else {
						sb.append(c);
					}
				}
			}
			ptr++;
		}

		if (dirty) {
			ret.add(sb.toString());
		}

		return ret;
	}


	private static void javaToolOptionsSuck(List<String> args) 
	{
		int i;
		String java_tool_options_suck = System.getenv("JAVA_TOOL_OPTIONS");
		if (java_tool_options_suck != null && java_tool_options_suck.length() > 0) {
			List<String> java_tool_options_suck_list = stringToArgs(java_tool_options_suck);
			int len = java_tool_options_suck_list.size();
			int arglen = args.size();
			int suckat = -1;
			if (arglen >= len) {
				boolean suck = false;
				String match = java_tool_options_suck_list.get(0);
				for(i=0;i<arglen;i++) {
					if (match.equals(args.get(i))) {
						suck = true;
						suckat = i;
					}
				}
				if (suck && ((suckat + len) <= arglen)) {
					for(i=0;i<len;i++) {
						args.remove(suckat);
					}
				}
			}
		}
	}

	public static String argsToString(List<String> args) {
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

	public static void applySudoArgs(List<String> args) 
	{
		String display = System.getenv("DISPLAY");
		boolean have_display = display == null ? false : display.length() > 0;
		
		File s;
		List<String> holdargs = new ArrayList<String>();
		holdargs.addAll(args);
		args.clear();

		if (SU.prefer_stdio) {
			// Try sudo
			s = new File("/usr/bin/sudo");
			if (s.canExecute()) {
				args.add("/usr/bin/sudo");
				args.addAll(holdargs);
				return;
			}
		}
		
		// Try gksudo
		if (have_display) {
			s = new File("/usr/bin/gksudo");
			if (s.canExecute()) {
				args.add("/usr/bin/gksudo");
				args.add("--description");
				args.add("Java Application");
				args.add(argsToString(holdargs));
				return;
			}
		}
		
		// Try kdesudo
		if (have_display) {
			s = new File("/usr/bin/kdesudo");
			if (s.canExecute()) {
				args.add("/usr/bin/kdesudo");
				args.add("-d");
				args.add("-c");
				args.add(argsToString(holdargs));
				args.add("--comment");
				args.add("Java application needs administrative privileges. Please enter your password");
				return;
			}
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
    	
    	ArrayList<String> pargs = new ArrayList<String>(len+2);
    	
    	pargs.add(command);
    	
    	for(int idx=0;idx<len;idx++) {
    		pargs.add(args[idx]);
    	}
    	
    	applySudoArgs(pargs);
    	
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
        
        	ArrayList<String> pargs = new ArrayList<String>();
        
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
				
				javaToolOptionsSuck(pargs);
				
				String[] cmd = jcmd.split("\\s+");
				
				if (cmd.length > 0 && cmd[0].endsWith(".jar")) {
					pargs.add("-jar");
				}
				
				for(int idx=0;idx<cmd.length;idx++) {
					pargs.add(cmd[idx]);
				}
			}

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

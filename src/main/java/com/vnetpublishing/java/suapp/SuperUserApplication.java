package com.vnetpublishing.java.suapp;

public abstract class SuperUserApplication 
 implements ISuperUserApplication {
	public final int sudo(String[] args) 
	{
		String os = SU.getOS();
		
		ISudo sudo = null;
		
		if ("windows".equals(os)) {
			sudo = new WinSudo();
		} else if ("linux".equals(os)) {
			sudo = new LinuxSudo();
		} else if ("mac".equals(os)) {
			sudo = new MacSudo();
		}
		
		if (null == sudo) {
			throw new IllegalStateException(String.format("Unsupported operating system: %s",os));
		}
		
		return sudo.sudo(args);
	}
	
	public final int sudo() 
	{
		return sudo(new String[]{});
	}
	
	public final boolean isSuperUser() 
	{
		
		String os = SU.getOS();
		
		ISuperUserDetector detector = null;
		
		if ("windows".equals(os)) {
			detector = new WinSuperUserDetector();
		} else if ("linux".equals(os)) {
			detector = new LinuxSuperUserDetector();
		} else if ("mac".equals(os)) {
			detector = new MacSuperUserDetector();
		}
		
		if (null == detector) {
			throw new IllegalStateException(String.format("Unsupported operating system: %s",os));
		}
		
		return detector.isSuperUser();
	}
}

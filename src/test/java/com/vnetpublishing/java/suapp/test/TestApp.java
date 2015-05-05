package com.vnetpublishing.java.suapp.test;

import java.lang.management.ManagementFactory;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;

import org.junit.Test;

import com.vnetpublishing.java.suapp.SU;
import com.vnetpublishing.java.suapp.SuperUserApplication;

import static org.junit.Assert.*;

public class TestApp extends SuperUserApplication {

	
	/*
	@Test
	public void sudoTest() {
		SU.setDaemon(true);
		int result = SU.run(this, new String[]{});
		assertEquals(0, result);
	}

	*/
	
	public int run(String[] args) 
	{
		// Never made it here...
		throw new RuntimeException("NOT IT!");
		//return 0;
	}
}

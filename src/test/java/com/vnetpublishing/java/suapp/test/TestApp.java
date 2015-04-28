package com.vnetpublishing.java.suapp.test;

import org.junit.Test;

import com.vnetpublishing.java.suapp.SU;
import com.vnetpublishing.java.suapp.SuperUserApplication;

import static org.junit.Assert.*;

public class TestApp extends SuperUserApplication {

	/*
	@Test
	public void sudoTest() {
		int result = SU.run(this, new String[]{});
		assertEquals(0, result);
	}

	*/
	
	public int run(String[] args) 
	{
		SU.setDaemon(true);
		return 0;
	}
}

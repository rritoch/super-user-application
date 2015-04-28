package com.vnetpublishing.java.suapp;

public interface ISuperUserApplication extends ISuperUserDetector, ISudo {

	int run(String[] args);

}

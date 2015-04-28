# super-user-application
Execute Jar's with Administrator privileges

### Example Usage:

```
package com.vnetpublishing.java;

import com.vnetpublishing.java.suapp.SU;
import com.vnetpublishing.java.suapp.SuperUserApplication;

public class TestAdmin extends SuperUserApplication {
	
	public static void main(String[] args) {
		SU.run(new TestAdmin(), args);
	}
	
	public int run(String[] args) {
		System.out.println("RUN AS ADMIN! YAY!");
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return 0;
	}
}


```

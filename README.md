# super-user-application
Execute Jar's with Administrator privileges

*Note:* On some versions of windows, elevated applications don't always have access to 
removable drives that were mounted by users. Applications that need to be elevated on windows
should be run from a non-removable hard drive. 

## Installation

Add the following dependency to your pom.xml

```
<dependency>
	<groupId>com.vnetpublishing.java</groupId>
	<artifactId>super-user-application</artifactId>
	<version>0.0.5</version>
</dependency>
```

## Building

Checkout this repository and execute the maven installer from the base directory with the pom.xml file in it.

```
mvn install
```

Once it is installed you can add it to your projects as a maven dependency.

### Maven Dependency
```
<dependency>
	<groupId>com.vnetpublishing.java</groupId>
	<artifactId>super-user-application</artifactId>
	<version>0.0.6-SNAPSHOT</version>
</dependency>
```
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

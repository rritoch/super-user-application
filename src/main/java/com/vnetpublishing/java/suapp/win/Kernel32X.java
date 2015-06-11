package com.vnetpublishing.java.suapp.win;

import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.WinBase;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.ptr.PointerByReference;
import com.sun.jna.win32.W32APIOptions;

import java.util.Arrays;
import java.util.List;

import com.sun.jna.Structure;
import com.sun.jna.platform.win32.BaseTSD.SIZE_T;
import com.sun.jna.platform.win32.BaseTSD.ULONG_PTR;
import com.sun.jna.platform.win32.WinDef.ULONGLONG;
import com.sun.jna.platform.win32.WinNT.LARGE_INTEGER;
import com.vnetpublishing.java.suapp.win.Shell32X.size_t;

public interface Kernel32X extends com.sun.jna.platform.win32.Kernel32 {
    Kernel32X INSTANCE = (Kernel32X) Native.loadLibrary("kernel32",
            Kernel32X.class, W32APIOptions.UNICODE_OPTIONS);

    HANDLE CreateJobObject(WinBase.SECURITY_ATTRIBUTES attrs, String name);

    boolean SetInformationJobObject(HANDLE hJob, int JobObjectInfoClass,
            Pointer lpJobObjectInfo, int cbJobObjectInfoLength);

    boolean AssignProcessToJobObject(HANDLE hJob, HANDLE hProcess);

    boolean TerminateJobObject(HANDLE hJob, long uExitCode);

    int ResumeThread(HANDLE hThread);

    public boolean IsProcessInJob(HANDLE ProcessHandle, HANDLE JobHandle, IntByReference Result);

    public HANDLE GetStdHandle(DWORD nStdHandle);

    boolean QueryInformationJobObject(HANDLE hJob,int JobObjectInfoClass,Pointer lpJobObjectInfo,int cbJobObjectInfoLength,IntByReference lpReturnLength);

    //Basic limit flags
    int JOB_OBJECT_BASIC_LIMIT_ACTIVE_PROCESS = 8;
    int JOB_OBJECT_BASIC_LIMIT_AFFINITY = 16;
    int JOB_OBJECT_BASIC_LIMIT_JOB_TIME = 4;
    int JOB_OBJECT_BASIC_LIMIT_PRIORITY_CLASS = 32;
    int JOB_OBJECT_BASIC_LIMIT_PRESERVE_JOB_TIME = 64;
    int JOB_OBJECT_BASIC_LIMIT_PROCESS_TIME = 2;
    int JOB_OBJECT_BASIC_LIMIT_SCHEDULING_CLASS = 128;
    int JOB_OBJECT_BASIC_LIMIT_WORKINGSET = 1;

    //Info classes
    int JOB_OBJECT_INFO_BASIC = 2;
    int JOB_OBJECT_INFO_EXTENDET = 9;
    int JOB_OBJECT_INFO_UI_RESTRICTIONS = 4;

    //extended limit flags
    int JOB_OBJECT_EXTENDED_LIMIT_BREAK_AWAY_OK = 2048;
    int JOB_OBJECT_EXTENDED_LIMIT_DIE_ON_UNHANDLED_EXCEPTION = 1024;
    int JOB_OBJECT_EXTENDED_LIMIT_JOB_MEMORY = 512;
    int JOB_OBJECT_EXTENDED_LIMIT_KILL_ON_JOB_CLOSE = 8192;
    int JOB_OBJECT_EXTENDED_LIMIT_PROCESS_MEMORY = 256;
    int JOB_OBJECT_EXTENDED_LIMIT_SILENT_BREAKAWAY_OK = 4096;

    //Job security flags
    int JOB_OBJECT_ALL_ACCESS = 2031647;
    int JOB_OBJECT_ASSIGN_PROCESS = 1;
    int JOB_OBJECT_QUERY = 4;
    int JOB_OBJECT_SET_ATTRIBUTES =2;
    int JOB_OBJECT_TERMINATE = 8;
    
    public static class IO_COUNTERS extends Structure {
    	/*
    	 typedef struct _IO_COUNTERS {
  		ULONGLONG ReadOperationCount;
  		ULONGLONG WriteOperationCount;
  		ULONGLONG OtherOperationCount;
  		ULONGLONG ReadTransferCount;
  		ULONGLONG WriteTransferCount;
  		ULONGLONG OtherTransferCount;
		} IO_COUNTERS, *PIO_COUNTERS; 
    	 
    	 */
    	
    	public ULONGLONG ReadOperationCount;
    	public ULONGLONG WriteOperationCount;
    	public ULONGLONG OtherOperationCount;
    	public ULONGLONG ReadTransferCount;
    	public ULONGLONG WriteTransferCount;
    	public ULONGLONG OtherTransferCount;
  		
        protected List getFieldOrder() {
            return Arrays.asList(new String[] {
            		"ReadOperationCount",
              		"WriteOperationCount",
              		"OtherOperationCount",
              		"ReadTransferCount",
              		"WriteTransferCount",
              		"OtherTransferCount",
            });
        }
    }

    public static class JOBOBJECT_EXTENDED_LIMIT_INFORMATION extends Structure 
    {
    	
    	class ByReference extends  JOBOBJECT_EXTENDED_LIMIT_INFORMATION implements Structure.ByReference { }
    	
    	
    	public JOBOBJECT_EXTENDED_LIMIT_INFORMATION() {
    		super();
    	}
    	
    	public JOBOBJECT_EXTENDED_LIMIT_INFORMATION(Pointer p) {
    		super(p);
    	}
    	
    	
    
    	/* typedef struct _JOBOBJECT_EXTENDED_LIMIT_INFORMATION {
    	  JOBOBJECT_BASIC_LIMIT_INFORMATION BasicLimitInformation;
    	  IO_COUNTERS                       IoInfo;
    	  SIZE_T                            ProcessMemoryLimit;
    	  SIZE_T                            JobMemoryLimit;
    	  SIZE_T                            PeakProcessMemoryUsed;
    	  SIZE_T                            PeakJobMemoryUsed;
    	} JOBOBJECT_EXTENDED_LIMIT_INFORMATION, *PJOBOBJECT_EXTENDED_LIMIT_INFORMATION;
    	*/
    	
    	public JOBOBJECT_BASIC_LIMIT_INFORMATION BasicLimitInformation;
    	public IO_COUNTERS                       IoInfo;
    	public size_t                            ProcessMemoryLimit;
    	public size_t                            JobMemoryLimit;
    	public size_t                            PeakProcessMemoryUsed;
    	public size_t                            PeakJobMemoryUsed;
  	  
  	  
      protected List getFieldOrder() {
          return Arrays.asList(new String[] {
        	  	  "BasicLimitInformation",
        	  	  "IoInfo",
        	  	  "ProcessMemoryLimit",
        	  	  "JobMemoryLimit",
        	  	  "PeakProcessMemoryUsed",
        	  	  "PeakJobMemoryUsed"
          });
      }
  	  
    }


public static class JOBOBJECT_BASIC_LIMIT_INFORMATION extends Structure {

	/*
	 typedef struct _JOBOBJECT_BASIC_LIMIT_INFORMATION {
LARGE_INTEGER PerProcessUserTimeLimit;
LARGE_INTEGER PerJobUserTimeLimit;
DWORD         LimitFlags;
SIZE_T        MinimumWorkingSetSize;
SIZE_T        MaximumWorkingSetSize;
DWORD         ActiveProcessLimit;
ULONG_PTR     Affinity;
DWORD         PriorityClass;
DWORD         SchedulingClass;
} JOBOBJECT_BASIC_LIMIT_INFORMATION, *PJOBOBJECT_BASIC_LIMIT_INFORMATION;
	 */
	
    public LARGE_INTEGER perProcessUserTimeLimit;


    public LARGE_INTEGER perJobUserTimeLimit;

    public int limitFlags;

    public SIZE_T minimumWorkingSetSize;


    public SIZE_T maximumWorkingSetSize;


    public int activeProcessLimit;

    public ULONG_PTR affinity;


    public int priorityClass;


    public int schedulingClass;


    @Override
    protected List<String> getFieldOrder() {
        return Arrays.asList(new String[] { "perProcessUserTimeLimit",
                "perJobUserTimeLimit", "limitFlags", "minimumWorkingSetSize",
                "maximumWorkingSetSize", "activeProcessLimit", "affinity",
                "priorityClass", "schedulingClass" });

    }

}

}

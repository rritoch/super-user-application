package com.vnetpublishing.java.suapp.win;

import java.util.Arrays;
import java.util.List;

import com.sun.jna.IntegerType;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import com.sun.jna.WString;
import com.sun.jna.platform.win32.BaseTSD.ULONG_PTR;
import com.sun.jna.platform.win32.Shell32;
import com.sun.jna.platform.win32.WinBase.SECURITY_ATTRIBUTES;
import com.sun.jna.platform.win32.WinDef.HINSTANCE;
import com.sun.jna.platform.win32.WinDef.HWND;
import com.sun.jna.platform.win32.WinDef.ULONGLONG;
import com.sun.jna.platform.win32.WinDef.WORD;
import com.sun.jna.platform.win32.WinNT.HANDLE;
import com.sun.jna.platform.win32.WinNT.LARGE_INTEGER;
import com.sun.jna.platform.win32.WinReg.HKEY;
import com.sun.jna.ptr.ByteByReference;
import com.sun.jna.win32.W32APIOptions;

public interface Shell32X extends Shell32
{
    Shell32X INSTANCE = (Shell32X)Native.loadLibrary("shell32", Shell32X.class, W32APIOptions.UNICODE_OPTIONS);

    int SW_HIDE = 0;
    int SW_MAXIMIZE = 3;
    int SW_MINIMIZE = 6;
    int SW_RESTORE = 9;
    int SW_SHOW = 5;
    int SW_SHOWDEFAULT = 10;
    int SW_SHOWMAXIMIZED = 3;
    int SW_SHOWMINIMIZED = 2;
    int SW_SHOWMINNOACTIVE = 7;
    int SW_SHOWNA = 8;
    int SW_SHOWNOACTIVATE = 4;
    int SW_SHOWNORMAL = 1;

    /** File not found. */
    int SE_ERR_FNF = 2;

    /** Path not found. */
    int SE_ERR_PNF = 3;

    /** Access denied. */
    int SE_ERR_ACCESSDENIED = 5;

    /** Out of memory. */
    int SE_ERR_OOM = 8;

    /** DLL not found. */
    int SE_ERR_DLLNOTFOUND = 32;

    /** Cannot share an open file. */
    int SE_ERR_SHARE = 26;



    int SEE_MASK_NOCLOSEPROCESS = 0x00000040;


    int ShellExecute(int i, String lpVerb, String lpFile, String lpParameters, String lpDirectory, int nShow);
    boolean ShellExecuteEx(SHELLEXECUTEINFO lpExecInfo);

     

    public static class size_t extends IntegerType {
        public size_t() { this(0); }
        public size_t(long value) { super(Native.SIZE_T_SIZE, value); }
    }
    

    

    /*
    public static class STARTUPINFO extends Structure {
    	/*
typedef struct _STARTUPINFO {
  DWORD  cb;
  LPTSTR lpReserved;
  LPTSTR lpDesktop;
  LPTSTR lpTitle;
  DWORD  dwX;
  DWORD  dwY;
  DWORD  dwXSize;
  DWORD  dwYSize;
  DWORD  dwXCountChars;
  DWORD  dwYCountChars;
  DWORD  dwFillAttribute;
  DWORD  dwFlags;
  WORD   wShowWindow;
  WORD   cbReserved2;
  LPBYTE lpReserved2;
  HANDLE hStdInput;
  HANDLE hStdOutput;
  HANDLE hStdError;
} STARTUPINFO, *LPSTARTUPINFO;
    	
    	
    	  int  cb;
    	  char[] lpReserved;
    	  char[] lpDesktop;
    	  char[] lpTitle;
    	  int  dwX;
    	  int  dwY;
    	  int  dwXSize;
    	  int  dwYSize;
    	  int  dwXCountChars;
    	  int  dwYCountChars;
    	  int  dwFillAttribute;
    	  int  dwFlags;
    	  WORD   wShowWindow;
    	  WORD   cbReserved2;
    	  Pointer lpReserved2;
    	  HANDLE hStdInput;
    	  HANDLE hStdOutput;
    	  HANDLE hStdError;
    	  
        protected List getFieldOrder() {
            return Arrays.asList(new String[] {
              	  "cb",
            	  "lpReserved",
            	  "lpDesktop",
            	  "lpTitle",
            	  "dwX",
            	  "dwY",
            	  "dwXSize",
            	  "dwYSize",
            	  "dwXCountChars",
            	  "dwYCountChars",
            	  "dwFillAttribute",
            	  "dwFlags",
            	  "wShowWindow",
            	  "cbReserved2",
            	  "lpReserved2",
            	  "hStdInput",
            	  "hStdOutput",
            	  "hStdError"
            });
        }
    }
    
    public static class PROCESS_INFORMATION extends Structure {
    	/*
    	 typedef struct _PROCESS_INFORMATION {
  HANDLE hProcess;
  HANDLE hThread;
  DWORD  dwProcessId;
  DWORD  dwThreadId;
} PROCESS_INFORMATION, *LPPROCESS_INFORMATION;
    	 
    	
    	HANDLE hProcess;
    	HANDLE hThread;
    	int  dwProcessId;
    	int  dwThreadId;
    	  
        protected List getFieldOrder() {
            return Arrays.asList(new String[] {
          	  	  "hProcess",
          	  	  "hThread",
          	  	  "dwProcessId",
          	  	  "dwThreadId"
            });
        }
    }
    
    */

    
    /*
    public static class SECURITY_ATTRIBUTES extends Structure
    {
		/*
			typedef struct _SECURITY_ATTRIBUTES {
			DWORD  nLength;
			LPVOID lpSecurityDescriptor;
			BOOL   bInheritHandle;
			} SECURITY_ATTRIBUTES, *PSECURITY_ATTRIBUTES, *LPSECURITY_ATTRIBUTES;
		
    	public int nLength = size();
    	public Pointer lpSecurityDescriptor;
    	public int bInheritHandle;
    	
        protected List getFieldOrder() {
            return Arrays.asList(new String[] {
                "nLength","lpSecurityDescriptor","bInheritHandle"
            });
        }
    }
    
    */
    
    public static class SHELLEXECUTEINFO extends Structure
    {
        /*
  DWORD     cbSize;
  ULONG     fMask;
  HWND      hwnd;
  LPCTSTR   lpVerb;
  LPCTSTR   lpFile;
  LPCTSTR   lpParameters;
  LPCTSTR   lpDirectory;
  int       nShow;
  HINSTANCE hInstApp;
  LPVOID    lpIDList;
  LPCTSTR   lpClass;
  HKEY      hkeyClass;
  DWORD     dwHotKey;
  union {
    HANDLE hIcon;
    HANDLE hMonitor;
  } DUMMYUNIONNAME;
  HANDLE    hProcess;
         */

        public int cbSize = size();
        public int fMask;
        public HWND hwnd;
        public String lpVerb;
        public String lpFile;
        public String lpParameters;
        public String lpDirectory;
        public int nShow;
        public HINSTANCE hInstApp;
        public Pointer lpIDList;
        public String lpClass;
        public HKEY hKeyClass;
        public int dwHotKey;

        /*
         * Actually:
         * union {
         *  HANDLE hIcon;
         *  HANDLE hMonitor;
         * } DUMMYUNIONNAME;
         */
        public HANDLE hMonitor;
        public HANDLE hProcess;

        protected List getFieldOrder() {
            return Arrays.asList(new String[] {
                "cbSize", "fMask", "hwnd", "lpVerb", "lpFile", "lpParameters",
                "lpDirectory", "nShow", "hInstApp", "lpIDList", "lpClass",
                "hKeyClass", "dwHotKey", "hMonitor", "hProcess",
            });
        }
    }

}

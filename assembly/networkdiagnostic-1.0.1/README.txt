
                          Network Diagnostic Tool (NDT)

  What is it?
  -----------

  NDT is a java standalone application designed for detecting network connectivity. It can
  be run on both Windows and Mac OS. Utilizing low level sockets, it can be used to 
  "ping" any host/port on the internet regardless of the protocols (HTTP/HTTPS), with 
  which the service is running on the port. NDT comes with all the dependencies 
  (includes versions of Java 1.8 for both Windows and Mac OS) and thus will be able to
  run out of box.  
   

  System Requirements
  -------------------

  JDK:
    1.8 or above (selection of this version was solely based on the desire to have the 
    possibility of utilizing the latest and greatest features in Java down the road.  
    However, the current version of this tool does not yet use any of 1.8 specific 
    features and thus can be recompiled with a lower version if needed).
  Memory:
    No minimum requirement.
  Disk:
    No minimum requirement. Approximately the package is 350MB.
  Operating System:
    No minimum requirement. On Windows, Windows NT and above or Cygwin is required for
    the startup scripts. Tested on Windows 7 and Mac OS X.

  Installing NDT
  ----------------

  1) Unpack the archive where you would like to store the binaries, eg:

    Unix-based Operating Systems (Linux, Solaris and Mac OS X)
      unzip networkdiagnostic-1.0.zip
    Windows
      unzip networkdiagnostic-1.0.zip

  2) A directory called "networkdiagnostic-1.0" will be created.

  Configuration
  -------------
  A subdirectory "conf" under directory "networkdiagnostic-1.0" contains the configuration 
  files:
  	conf.txt		this file is for specifying your network targets/sites to test for
  					connectivity.
  					
  					Each line in the file denotes a network target/sites you want to 
  					test in the following format:  					
					1. 		<host>[:port]
					2.		<IP>[:port]
					The port part is optional; when it is not specified, default 80 is 
					used. When an IP is specified, only that IP gets tested.  When a host 
					is specified such as “sam-cdn.education.scholastic.com”, then all its 
					IPs will be looked up and tested.
 
					Example:
						www.google.com   
						www.gmail.com:433
						173.194.123.22:433
						173.194.123.21
						Sam-cdn.education.scholastic.com

  					You can specify the sites in two ways.  One is to put them in the 
  					configuration file. And the other is to specify them on the command 
  					line.  The later one might be useful in the case where you just want 
  					to quickly check couple sites without modifying your configuration 
  					file.

  					
  	log4j2.xml  	this file defines the format and verbosity of the output. 

  Run NDT
  ----------------
  The package comes with two scripts, one for Windows and the other for MacOS
    Unix-based Operating Systems (Linux, Solaris and Mac OS X)
    	script name: ndt.sh
    Windows
    	script name: ndt.bat
    	
    usage: ndt
 		-c,--config <config file path>   configuration file path. If it is not
                                  		 specified through this option, the
                                  		 default one conf.txt in the directory
                                  		 conf is used.
                                  		 
 		-h,--help                        print this message.
 		
 		-n,--numberthreads <arg>         number of threads to run the tool
                                  	 	 concurrently. Default 1 thread.
                                  	 	 
 		-s,--site <site1, site2, ...>    site(s) seperated by comma to test for
                                  		 connectivity, e.g.
                                  		 sam-cdn.education.scholastic.com:80,
                                  		 www.google.com, etc.. If it is not
                                  		 specified, the ones defined in the
                                  		 conf.txt file will be tested.
                                  		 
 		-t,--timeout <seconds>           time in seconds to wait before giving up
                                  		 establishing a connection to a network
                                  		 host. If it is not specified, 3 seconds
                                  		 will be the default value.


    The output of the tool defaults to console. But that can be changed through the
    log4j2.xml to route to a file.
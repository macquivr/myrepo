package com.example.demo.importer;

import java.io.*;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class checkUtil {
	private static final Logger log = LoggerFactory.getLogger(checkUtil.class);
	private static checkUtil obj = null;
	
	private final String cpath;
	private final String dir;
	
	private checkUtil()
	{
		this.dir = System.getenv("MYFI_DATA_DIR");
		this.cpath = "checks";
	}
	
	public String getDir() { return dir; }
	public String getCPath() { return cpath; }
	
	public static checkUtil getObj()
	{
		if (obj == null)
			obj = new checkUtil();
		return obj;
	}
	
	public boolean updateCheck(String num, String payee)
	{
		Properties p = readChecks();
		if (p == null) {
			log.info("Could not read checks.");
			return false;
		}
		
		p.put(num, payee);
		
		File f = new File(dir,cpath);
		if (!f.exists()) {
			log.info("Could not update checks.");
			return false;
		}
		
		try {
			FileWriter w = new FileWriter(f);
			p.store(w, "");
		} catch (Exception ex) {
			log.error("Could not write to checks file.");
			return false;
		}
	
		return true;
	}
	
	public Properties readChecks()
	{
		Properties p = new Properties();
		
		if (cpath == null) {
			log.error("NULL CPATH....");
			return null;
		}
		
		File f = new File(dir,cpath);
		if (!f.exists()) {
			log.info("No Checks....");
			return null;
		}
		try {
			p.load(new FileReader(f));
		} catch (Exception e) {
			log.error("Bad Checks " + e.getMessage());
			return null;
		}
		return p;
	}
}

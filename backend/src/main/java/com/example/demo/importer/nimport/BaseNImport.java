package com.example.demo.importer.nimport;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Vector;
import java.util.StringTokenizer;

import com.example.demo.utils.FileUtils;

public abstract class BaseNImport {
	protected List<String> lines = null;
	protected List<String> dates = null;
	protected List<String> labels = null;
	protected List<Double> credit = null;
	protected List<Double> debit = null;
	protected List<Integer> checks = null;
	protected List<String> out = null;
	protected List<NData> ndata = null;
	protected String dir = null;
	protected String csv = null;
	protected int numLines = 0;
	protected int numColumns = 0;
	protected int elines = 0;
	
	public BaseNImport(String dir,String csv)
	{
		this.dir = dir;
		this.csv = csv;
		ndata = new Vector<NData>();
		lines = new Vector<String>();
		dates = new Vector<String>();
		labels = new Vector<String>();
		credit = new Vector<Double>();
		debit = new Vector<Double>();
		checks = new Vector<Integer>();
		out = new Vector<String>();
	}
	
	public boolean doImport()
	{
		boolean r = read();
		if (!r) 
			return false;
		
		if (!validate()) 
			return false;

		go();
		
		return write();
	}
	
	public boolean validate()
	{
		if (numLines > 0) {
			if (lines.size() <= numLines) {
				System.out.println("Not enough lines " + csv);
				return false;
			}
		}
		
		int i = 0;
		for (i=0;i<numLines;i++)
			lines.remove(0);
		
		if (elines > 0) {
			for (i=0;i<elines;i++) 
				lines.remove(lines.size()-1);
		}
				
		List<String> nlines = new Vector<String>();
		for (String line : lines) {
			line = fixCommas(line);
			line = line.replace(",,",",X,");
			if (line.endsWith(","))
				line = line.concat("X");
			
			StringTokenizer st = new StringTokenizer(line,",");
			if (st.countTokens() != numColumns) {
				System.out.println("Invalid token count " + csv + " " + line);
				return false;
			}
			nlines.add(line);
		}
		
		lines = nlines;
		return true;
	}
	
	public String fixCommas(String line)
	{
		String ret = "";
		int i = 0;
		boolean on = false;
		char nxt = 0;
		
		for (i=0;i<line.length();i++) {
			nxt = line.charAt(i);
			if (on) {
				if (line.charAt(i) == ',') 
					nxt = ' ';
				if (line.charAt(i) == '"') 
					on = false;
			} else {
				if (line.charAt(i) == '"')
					on = true;
			}
			
			ret = ret.concat(String.valueOf(nxt));
		}
		return ret;
	}
	public abstract void go(); 
	public List<NData> getNData() { return ndata; }
	public List<String> getOut() { return out; }
	protected boolean write()
	{
		File f = new File(dir + "/gen/" + csv);
		try {
			FileUtils.writeFile(out, f);
		} catch (Exception ex) {
			return false;
		}
		return true;
	}
	protected boolean read()
	{
		File f = new File(dir + "/" + csv);
		String fileStr = null;
		
		try {
			fileStr = FileUtils.readFile(f);
		} catch (IOException ex) {
			ex.printStackTrace();
			return false;
		}
		
		StringTokenizer st = new StringTokenizer(fileStr,"\n");

		while (st.hasMoreTokens()) {
			String str = st.nextToken();
			lines.add(str);
		}
		return true;
	}
	
}

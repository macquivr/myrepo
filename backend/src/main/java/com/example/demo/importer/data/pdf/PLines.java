package com.example.demo.importer.data.pdf;

import com.example.demo.importer.BadDataException;
import com.example.demo.importer.IBase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;
import java.util.Vector;
import java.util.StringTokenizer;

public abstract class PLines {
	private static final Logger log = LoggerFactory.getLogger(PLines.class);
	protected List<String> lines;
	
	public PLines(IBase obj) throws BadDataException
	{
		lines = new Vector<>();
		makeLines(obj.readPdf(),getLabel());
	}

	protected abstract String getLabel();

	private void makeLines(String txt, String label)
	{
		lines = new Vector<>();
		StringTokenizer st = new StringTokenizer(txt,"\n");
		while (st.hasMoreTokens()) {
			String l = st.nextToken();
			lines.add(l);
		}
	}
}

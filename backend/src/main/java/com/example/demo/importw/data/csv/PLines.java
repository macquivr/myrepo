package com.example.demo.importw.data.csv;

import com.example.demo.bean.StartStop;
import com.example.demo.importer.BadDataException;
import com.example.demo.importer.IBase;
import com.example.demo.importer.checkUtil;
import com.example.demo.importw.IBasew;
import com.example.demo.utils.mydate.DUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.StringTokenizer;

public abstract class PLines {
	private static final Logger log = LoggerFactory.getLogger(PLines.class);
	protected List<String> lines;
	protected StartStop dates;

	public PLines(IBasew obj) throws BadDataException
	{
		lines = new Vector<>();
		initStartStop();
		makeLines(obj.readFile(),getLabel());
	}

	protected abstract String getLabel();

	protected void initStartStop() {
		this.dates = new StartStop();
		try {
			String fileName = checkUtil.getObj(true).getDir() + "/" + "dates.txt";
			String ds = new String(Files.readAllBytes(Paths.get(fileName)));

			List<String> dsl = new Vector<>();
			StringTokenizer st = new StringTokenizer(ds,"\n");
			while (st.hasMoreTokens()) {
				String l = st.nextToken();
				dsl.add(sanitize(l));
			}
			System.out.println("** START " +  dsl.get(0) + " **");
			System.out.println("** STOP " +  dsl.get(1) + " **");
			this.dates.setStart(DUtil.getStdDate(dsl.get(0)));
			this.dates.setStop(DUtil.getStdDate(dsl.get(1)));

			if (dates.getStart() == null) {
				System.out.println("*** COULD NOT SET START DATE!!!");
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	private void makeLines(String txt, String label)
	{
		lines = new Vector<>();
		StringTokenizer st = new StringTokenizer(txt,"\n");
		while (st.hasMoreTokens()) {
			String l = st.nextToken();
			log.info("LINE " + label + ": " + l);
			lines.add(sanitize(l));
		}
	}

	private String sanitize(String str) {
		String ret = "";
		boolean on = false;
		int len = str.length();
		int i = 0;
		for (i=0;i<len;i++) {
			char c = str.charAt(i);
			if (c == '"') {
				on = !on;
			} else {
				if (!on || (c != ',')) {
					ret = ret.concat(String.valueOf(c));
				}
			}
		}

		return ret;
	}
}

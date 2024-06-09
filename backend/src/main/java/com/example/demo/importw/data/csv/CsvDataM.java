package com.example.demo.importw.data.csv;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import com.example.demo.importer.data.NData;
import com.example.demo.importw.IBasew;
import com.example.demo.importw.data.IData;
import com.example.demo.utils.mydate.DUtil;
import java.util.StringTokenizer;
import java.util.Vector;
import com.example.demo.importer.Repos;
import com.example.demo.utils.Utils;
import com.example.demo.domain.Mltype;
import com.example.demo.importer.BadDataException;
import com.example.demo.utils.FileUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CsvDataM extends PBase {
private static final Logger log = LoggerFactory.getLogger(CsvDataM.class);
	private double start = 0;
	private double stop = 0;
	private double target = 0;
	private int transl = 0;
	private int transm = 0;
	private int year = 0;
	private final IData idata;
	private List<String> mlines;
	private final String dir;

	private final Repos repos;

	public CsvDataM(IBasew obj, IData data, String d) throws BadDataException {
		super(obj,data);

		repos = obj.getRepos();
		dir = d;
		idata = data;
	}

	public String getLabel() { return "ML"; }

	public String transform(String str) { return ""; }
	public NData ledgerLine(String[] tokens) throws BadDataException
	{
	/*
	"Trade Date" ,"Settlement Date" ,"Account" ,"Description" ,"Type" ,"Symbol/ CUSIP" ,"Quantity" ,"Price" ,"Amount" ," "
	0 - tdate
	1 - fnord
	2 - fnord
	3 - label
    4 - fnord
    5 - fnord
    6 - fnord
    7 - fnord
    8 - amount
		 */
		LocalDate ds = DUtil.getStdDate(tokens[0].trim());
		if (ds == null) {
			System.out.println("EEK DS ML!");
			return null;
		}
		if (dates.getStart() == null) {
			System.out.println("EEK ML!");
			return null;
		}
		if ((ds.isBefore(this.dates.getStart())) ||
				(ds.isAfter(this.dates.getStop()))) {
			return null;
		}

		NData ret = new NData();

		ret.setDate(tokens[0]);
		System.out.println("** " + tokens[3] + "**");
		if (tokens[3].startsWith("Check ")) {
			ret.setLabel("Check");
			int idx = tokens[3].indexOf(' ');
			String str = tokens[3].substring(idx);
			try {
				int cnum = Integer.valueOf(str);
				ret.setCheck(cnum);
			} catch (NumberFormatException ex) {
				System.out.println("EEK *" + str);
			}
		} else {
			ret.setLabel(tokens[3]);
		}
		String amount = tokens[8].replaceAll("\\$","");

		char t = amount.charAt(0);

		if (t != '-') {
			ret.setCredit(Double.parseDouble(amount));
		} else {
			ret.setDebit(Double.parseDouble(amount));
		}
		return ret;
	}
	
	private double getNet(List<NData> nl)
	{
		double net = 0;
		double tin = 0;
		double tout = 0;
		
		for (NData n : nl) {
			tin = Utils.dvAdd(tin, n.getCredit());
			tout = Utils.dvAdd(tout, n.getDebit());
			
			net = Utils.dvAdd(net, n.getCredit());
			net = Utils.dvSub(net, n.getDebit());
			log.info("MLAMT: " + n.getDate() + " " + n.getCredit() + " " + n.getDebit() + " " + tin + " " + tout + " " + net);
		}
		return net;
	}
	
	private void checkTarget() throws BadDataException
	{
		List<NData> nl = idata.getData();		
		double net = getNet(nl);
		if (net != target)
			throw new BadDataException("Target mismatch " + target + " " + net);
	}
	
	private void doIn() throws BadDataException
	{
		int lidx = 0;
		String lstr = null;
		for (String s : lines) {
			if (lidx < transl) {
				lidx++;
				continue;
			}

			if (s.startsWith("CASH/OTHER TRANSACTION")) 
				return;
			String dstr = isDate(s);
			if (dstr != null)
				lstr = dstr;
			if (s.contains("ML BANK DEPOSIT PROGRAM")) {
				if (lstr != null) {
					String t = trim(s);
					if (t != null) {
						s = lstr.concat(t);
					}
				}
			}
			addNData(s,false,false);
		}
	}
	
	private String isDate(String lstr) 
	{
		String s = trim(lstr);
		if (s == null)
			return null;
		int idx = s.indexOf(' ');
		if (idx == -1)
			return null;
		String str = s.substring(0,idx);
		if (!DUtil.isValidMMDD(str))
			return null;

		return str;
	}

	private String mendCheckNum(String cstr)
	{
		int cnt = 0;
		if (cstr.endsWith("*")) {
			cstr = cstr.substring(0,cstr.length()-1);
		}
		byte[] b = cstr.getBytes();
		for (int i = 0;i<b.length;i++)
			if (b[i] < 0)
				cnt++;

		return (cnt == 0) ? cstr : cstr.substring(0,cstr.length()-1);
	}

	private NData makeNData(String str, String rest) {
		int idx = rest.indexOf(' ');
        if (idx == -1)
        	return null;
		NData ret = new NData();

        String dstr = rest.substring(0,idx);
		String ndstr;

		if (DUtil.isValidMMDD(dstr)) {
			ndstr = dstr + "/" + year;
		} else {
			if (!DUtil.isValidMMDD(str))
				return null;
			ndstr = str + "/" + year;
		}
		ret.setDate(ndstr);
		ret.setNDstr(ndstr);

		return ret;
	}
	private void addNData(String lstr, boolean sd, boolean check) throws BadDataException
	{
		String ndstr;
		String s = trim(lstr);
		if (s == null)
			return;
		int idx = s.indexOf(' ');
		if (idx == -1)
			return;
		String str = s.substring(0,idx);
		String rest = s.substring(idx+1);

		NData n = makeNData(str,rest);
		if (n == null)
			return;

		idx = findValue(rest);
		if (idx == -1) {
			throw new BadDataException("Couldn't find value for " + lstr + " *** " + str + " ***" + rest);
		}
		String v = rest.substring(idx+1);
		v = v.replaceAll(" ", "");
		v = v.replaceAll(",", "");
		if (v.startsWith("(")) {
			v = v.substring(1,v.length()-2);
		}

		String lbl = rest.substring(0,idx);

		if (lbl.startsWith("MONTH END SUMMARY"))
			return;
		if (lbl.equals("OPENING BALANCE") || (lbl.equals("CLOSING BALANCE")))
			return;
		
		if (check) {
			idx = lbl.indexOf(' ');
			if (idx == -1) {

				throw new BadDataException("Couldn't parse check line " + lstr);
			}
			String cstr = mendCheckNum(lbl.substring(0,idx));

			int cnum = 0;
			boolean err = false;
			try {
				cnum = Integer.parseInt(cstr);
			} catch (Exception ex) {
				err = true;
				int nidx = lbl.indexOf(' ');
				if (nidx != -1) {
					String nstr = lbl.substring(nidx + 1);
					nidx = nstr.indexOf('#');
					nstr = nstr.substring(0, nidx);
					try {
						cnum = Integer.parseInt(nstr);
						err = false;
					} catch (Exception nex) {
						// fall thru
					}
				}
			}
			if (err) {
				throw new BadDataException("Bad Check num " + cstr);
			}
			n.setCheck(cnum);
			lbl = "Check";
		} else {
			if (sd) {
				 idx = lbl.indexOf(' ');
                 ndstr = lbl.substring(0,idx) + "/" + year;
                 lbl = lbl.substring(idx+1);
                 n.setNDstr(ndstr);
			}
		}

		boolean ok = true;
		n.setLabel(mendLabel(lbl.trim()));
        if (lbl.contains("Deposit")) {
			Double d = Utils.dval(v);
			if (d != null) {
				n.setCredit(d);
			}
		}
        else {
        	if ((lbl.contains("Withdrawal") || lbl.equals("Check"))) {
				Double d = Utils.dval(v);
				if (d != null) {
					n.setDebit(d);
				}
			}
			else
				ok = determineType(n,v);
        }

		if (ok)
			idata.getData().add(n);
	}
	
	public void readLines() throws BadDataException
	{
		String fileStr;
		File f = new File(dir,"ml.csv");
		
		try {
			fileStr = FileUtils.readFile(f);
		} catch (IOException e) {
			throw new BadDataException("Problem with ML csv");
		}
		
		mlines = new Vector<>();
		StringTokenizer st = new StringTokenizer(fileStr,"\n");

		while (st.hasMoreTokens()) {
			String line = st.nextToken();
			mlines.add(line.replaceAll(",", ""));
		}
	}
	
	private boolean determineType(NData n, String v)
	{
		if (n.getLabel().contains("BANK DEPOSIT INTEREST")) {
			n.setCredit(Double.parseDouble(v));
			return true;
		}
		
		String l = null;
		for (String s : mlines) {
			int idx = 0;
			String dstr2 = null;
			if (!s.isEmpty()) {
				StringTokenizer st = new StringTokenizer(s,"\"");
				while (st.hasMoreTokens()) {
					String str = st.nextToken().trim();
					if (idx == 2) {
						dstr2 = str;
					}
					
					idx++;
				}
			}
		
			if (s.contains(v) && ((n.getNDstr() == null) || ((dstr2 != null) && (dstr2.equals(n.getNDstr())))))
				l = s;
		}

		if (l == null)  {
			log.info(n.getLabel() + " not found, skipping....");
			return false;
		}

		StringTokenizer st = new StringTokenizer(l,"\"");
		int idx = 0;
		String vstr = null;
		boolean f = false;
		String lbl = n.getLabel();
		if ((lbl.contains(" Sale ")) || (lbl.contains(" Purchase "))) {
			f = true;
		}
		while (st.hasMoreTokens()) {
			String str = st.nextToken().trim();
			
			if (!f && (idx == 12) && (!str.isEmpty()))
				vstr = str;
			
			if (!f && (idx == 13) && (vstr == null)) 
				vstr = str;

			if (f && (idx == 15))
				vstr = str;
			
			idx++;
		}

		if (vstr == null) {
			log.info("Couldn't find value " + n.getLabel() + " " + v + " Skipping...");
			return false;
		}

		if (vstr.startsWith("$")) {
			Double d= Utils.dval(vstr.substring(1));
			if (d != null) {
				n.setCredit(d);
			}
		} else {
			Double d = Utils.dval(vstr.substring(2));
			if (d != null) {
				n.setDebit(d);
			}
		}
		return true;
	}
	
	private String mendLabel(String lbl) 
	{
		List<Mltype> types = repos.getMlTypeRepository().findAll();
		
		String match = null;
		
		for (Mltype t : types) {
			if (lbl.endsWith(t.getName())) {
				if (match == null)
					match = t.getName();
				else {
					if (t.getName().length() > match.length())
						match = t.getName();
				}
			}
		}
		
		if (match != null) {
			String ret = lbl.replaceAll(match,"");
			lbl =  ret.substring(0,ret.length()-1);
		}
		
		return lbl;
	}

	private void doTrans() throws BadDataException
	{
		int lidx = 0;
		boolean on = false;
		for (String s : lines) {
			if (lidx < transl) {
				lidx++;
				continue;
			}
			if (!on && !s.startsWith("CASH/OTHER TRANSACTION")) { 
				lidx++;
				continue;
			}
			if (s.isEmpty())
				continue;

			on = true;
			if (s.startsWith("VISA ACCESS CARD ACTIVITY")) 
				return;
			if (s.startsWith("CHECKS WRITTEN")) 
				return;
			if (s.startsWith("YOUR CMA MONEY ACCOUNT TRANSACTIONS")) 
				return;
			String str = trim(s);
			if (str != null) {
				addNData(str,false,false);
			}
		}
	}
	
	private String trim(String data) 
	{
		String ret = data;
		if ((ret == null) || (ret.isEmpty()))
			return null;
		
		while (ret.charAt(0) == ' ')
		{
			ret = ret.substring(1);	
			if (ret.isEmpty())
				return null;
		}
		
		if (ret.charAt(0) == '.')
			ret = ret.substring(1);
		
		if (ret.isEmpty())
			return null;
		
		return ret;
	}
	
	private void doVisa() throws BadDataException
	{
		int lidx = 0;
		boolean on = false;
		for (String s : lines) {
			if (lidx < transl) {
				lidx++;
				continue;
			}
			if (!on && !s.startsWith("VISA ACCESS CARD ACTIVITY")) { 
				lidx++;
				continue;
			}
			on = true;
			if (s.startsWith("CHECKS WRITTEN")) 
				return;
			if (s.startsWith("YOUR CMA MONEY ACCOUNT TRANSACTIONS")) 
				return;
			String str = trim(s);
			addNData(str,true,false);
		}		
	}
	
	private void doChecks() throws BadDataException
	{
		int lidx = 0;
		boolean on = false;
		for (String s : lines) {
			if (lidx < transl) {
				lidx++;
				continue;
			}
			if (lidx > transm)  {
				return;
			}
			if (!on && !s.startsWith("CHECKS WRITTEN")) {
				lidx++;
				continue;
			}
			on = true;
			if (s.startsWith("NET TOTAL")) 
				return;
			String str = trim(s);
			if (str != null) {
				addNData(str, true, true);
			}
		}
	}
	
	private void doStartStop() throws BadDataException
	{
		int cnt = 0;
		int lidx = 0;
		transl = -1;
		start = -1;
		stop = -1;
		year = 0;
		log.info("MLINES: " + lines.size());
		boolean on = false;
		for (String s : lines) {
			log.info("DLINE: " + s);
			if (s.startsWith("YOUR CMA TRANSACTIONS") && (transl == -1)) 
				transl = lidx;
			if (s.contains("INVESTMENT ACCOUNT") && (transm == -1)) {
				this.transm = lidx;
			}
			if ((s.startsWith("YOUR MERRILL LYNCH REPORT") || 
			    (s.startsWith("WEALTH MANAGEMENT REPORT")))) {
				int didx = s.indexOf(',');
				String str = s.substring(didx+2);
				didx = str.indexOf(',');
				str = str.substring(didx+2);
                year = Integer.parseInt(str);
				log.info("MLYEAR: " + year);
			}
			if (on) {
				try {
					log.info("START: " + s);
					int idx = s.indexOf('$');
					if (idx == -1) {
						start = 92.78;
						log.info("STARTV: " + start);
					} else {
						String str = s.substring(idx + 1);
						str = str.replaceAll(",", "");
						if (str.endsWith(")")) {
							str = "-" + str.substring(0, str.length() - 1);
						}
						log.info("STARTV: " + str);
						Double d = Utils.dval(str);
						if (d != null) {
							start = d;
						}
					}
				} catch (Exception ex) {
					throw new BadDataException("Bad Start " + s);
				}
				on = false;
			}
			if (s.startsWith("CASH FLOW")) {
				if (cnt == 1) 
					on = true;
				cnt++;
			}
			if (s.startsWith("Closing Cash")) {
				if (cnt == 2) {
					try {
						log.info("STOP: " + s);
						int idx = s.indexOf('$');
						String str = s.substring(idx+1);
						str = str.replaceAll(",","");
						if (str.endsWith(")")) {
							str = "-" + str.substring(0,str.length()-1);
						}
						log.info("STOPV:" + str);
						Double d = Utils.dval(str);
						if (d != null) {
							stop = d;
						}
					} catch (Exception ex) {
						stop = 0;
						//throw new BadDataException("Bad Stop " + s);
					}
				}
			}
			lidx++;
		}
		if (start == -1) {
			throw new BadDataException("Could not find start.");
		}
		if (stop == -1) {
			throw new BadDataException("Could not find stop.");
		}
		if (year == 0) {
			throw new BadDataException("Could not find year.");
		}
	}
	
}

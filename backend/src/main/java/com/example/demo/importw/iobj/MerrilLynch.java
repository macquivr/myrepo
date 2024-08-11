package com.example.demo.importw.iobj;

import com.example.demo.domain.Payperiod;
import com.example.demo.domain.Statement;
import com.example.demo.domain.Statements;
import com.example.demo.dto.ImportDTO;
import com.example.demo.importer.*;
import com.example.demo.importw.data.csv.CsvDataM;
import com.example.demo.importw.IBasew;

import java.util.UUID;
import java.util.List;

public class MerrilLynch extends IBasew {
	private Payperiod pp = null;

	public MerrilLynch(UUID uuid, Repos r, ImportDTO dto)
	{	
		super(uuid,r,dto);

		fname = "ml.csv";
		ltype = 11;
		credit = false;
	}
	
	public boolean validateFile(List<String> ret) { 
		try {
			new CsvDataM(this,data,checkUtil.getObj(true).getDir()).go();
		} catch (BadDataException ex) {
			ret.add("Bad Data " + ex.getMessage());
			return false;
		}
		return doChecks(ret);
	}
	
	public boolean makeData(List<String> err)
	{
		SaveO obj = new SaveO(this, ltype, data, imdata,err, repos.getChecksRepository(), this.pp);
	
		return obj.makeData();
	}

}

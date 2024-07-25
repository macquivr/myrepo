package com.example.demo.importw.iobj;

import com.example.demo.domain.Payperiod;
import com.example.demo.domain.Stype;
import com.example.demo.dto.ImportDTO;
import com.example.demo.importer.BadDataException;
import com.example.demo.importer.Repos;
import com.example.demo.importer.data.NData;
import com.example.demo.importw.data.csv.*;
import com.example.demo.importw.IBasew;

import java.util.UUID;
import java.util.List;

public abstract class CBasew extends IBasew {
	private Payperiod pp = null;

	public abstract PBase makePBase() throws BadDataException;
	public CBasew(UUID uuid, Repos r, ImportDTO dto, Payperiod pp)
	{
		super(uuid,r, dto);
		this.pp = pp;
	}

	public boolean validateFile(List<String> ret) { 
		try {
			makePBase().go();
		} catch (BadDataException ex) {
			ret.add("Bad Data " + ex.getMessage());
			return false;
		}

		return true;
	}
	
	@Override
	public void setStype(List<String> ret)
	{
		for (NData d : data.getData()) {
			Stype m = repos.getStypeRepository().findByName("CreditTrans");
			d.setStype(m);
		}
	}
	
	public boolean makeData(List<String> err)
	{
		SaveO obj = new SaveO(this, ltype, data, imdata,err, repos.getChecksRepository(), this.pp);
	
		return obj.makeData();
	}

}

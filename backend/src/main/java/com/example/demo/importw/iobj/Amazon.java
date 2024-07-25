package com.example.demo.importw.iobj;

import java.util.UUID;

import com.example.demo.domain.Payperiod;
import com.example.demo.dto.ImportDTO;
import com.example.demo.importer.BadDataException;
import com.example.demo.importer.Repos;
import com.example.demo.importw.data.csv.CsvDataAmz;
import com.example.demo.importw.data.csv.PBase;

public class Amazon extends CBasew {
	
	public Amazon(UUID uuid, Repos r, ImportDTO dto, Payperiod pp)
	{	
		super(uuid,r,dto, pp);
		
		fname = "amazon.csv";
		ltype = 9;
		credit = true;
	}
	
	public PBase makePBase() throws BadDataException {
		return new CsvDataAmz(this,data);
	}
	
}

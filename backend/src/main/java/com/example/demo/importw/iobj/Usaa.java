package com.example.demo.importw.iobj;


import com.example.demo.domain.Payperiod;
import com.example.demo.dto.ImportDTO;
import com.example.demo.importer.BadDataException;
import com.example.demo.importer.Repos;
import com.example.demo.importw.data.csv.CsvDataUsaa;
import com.example.demo.importw.data.csv.PBase;

import java.util.UUID;

public class Usaa extends CBasew {
	
	public Usaa(UUID uuid, Repos r, ImportDTO dto)
	{	
		super(uuid, r,dto);
		
		fname = "usaa.csv";
		ltype = 8;
		credit = true;
	}
	
	public PBase makePBase() throws BadDataException {
	    return new CsvDataUsaa(this,data);
	}
	
}

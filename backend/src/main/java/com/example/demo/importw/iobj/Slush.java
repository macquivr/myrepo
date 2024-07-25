package com.example.demo.importw.iobj;

import com.example.demo.domain.Payperiod;
import com.example.demo.dto.ImportDTO;
import com.example.demo.importer.CsbEType;
import com.example.demo.importer.Repos;
import java.util.UUID;

public class Slush extends CSBasew {

	public Slush(UUID uuid, Repos r, ImportDTO dto, Payperiod pp)
	{	
		super(uuid,r,dto,pp);

		etype = CsbEType.SLUSH;
		fname = "Slush.csv";
		ltype = 12;
		credit = false;
	}
}

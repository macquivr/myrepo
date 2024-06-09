package com.example.demo.importw.iobj;

import com.example.demo.domain.Statement;
import com.example.demo.domain.Statements;
import com.example.demo.dto.ImportDTO;
import com.example.demo.importer.CsbEType;
import com.example.demo.importer.Repos;
import java.util.UUID;

public class Slush extends CSBasew {

	public Slush(UUID uuid, Repos r, ImportDTO dto)
	{	
		super(uuid,r,dto);

		etype = CsbEType.SLUSH;
		fname = "Slush.csv";
		ltype = 12;
		credit = false;
	}
}

package com.example.demo.importer.data;

import java.util.UUID;
import java.util.List;
import java.util.Vector;
import com.example.demo.domain.*;
import com.example.demo.importer.*;

public class TypeHelper extends importBase {
	private static TypeHelper obj = null;
	
	private final List<Integer> eids;
	private final List<Integer> cids;
	private final Repos repos;

	public static TypeHelper getObj(UUID uuid, Repos repos)
	{
		if (obj == null)
			obj = new TypeHelper(uuid, repos);
		obj.setUuid(uuid);
		return obj;
	}
	
	private TypeHelper(UUID uuid, Repos r)
	{
		super(uuid);

		repos = r;
		eids = new Vector<>();
		cids = new Vector<>();
		init();
	}
	
	private void init()
	{
		List<Csbtype> types = repos.getCsbTypeRepository().findAll();
		
		for (Csbtype c : types) {
			if (c.getName().equals("Deposit")) 
				eids.add(c.getId());
			if (c.getName().equals("Withdrawal")) 
				eids.add(c.getId());
			if (c.getName().equals("Check")) 
				cids.add(c.getId());
			if (c.getName().equals("Over Counter Check")) 
				cids.add(c.getId());
			if (c.getName().equals("Check - Item Processing"))
				cids.add(c.getId());
		}
		
	}
	
	public List<Integer> getEids() { return eids; }
	public List<Integer> getCids() { return cids; }
}

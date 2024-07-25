package com.example.demo.importw.iobj;

import com.example.demo.dto.ImportDTO;
import java.util.UUID;
import java.util.List;

import com.example.demo.importer.BadDataException;
import com.example.demo.importer.CsbEType;
import com.example.demo.importer.Repos;
import com.example.demo.importer.data.NData;
import com.example.demo.importer.data.TypeHelper;
import com.example.demo.importw.IBasew;
import com.example.demo.importw.data.CsvData;
import com.example.demo.utils.mydate.DUtil;
import java.util.Vector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.example.demo.domain.*;

public abstract class CSBasew extends IBasew {
	private Payperiod pp = null;
	private static final Logger log = LoggerFactory.getLogger(CSBasew.class);
	protected CsbEType etype = null;

	public CSBasew(UUID uuid, Repos r, ImportDTO dto, Payperiod pp)
	{
		super(uuid,r, dto);
		this.pp = pp;
	}
	
	public boolean validateFile(List<String> ret)
	{
		try {
			CsvData obj = new CsvData(this,data, etype);
			obj.go();
			if (obj.isSkip()) {
				this.skip = true;
				return true;
			}
		} catch (BadDataException ex) {
			ret.add("Bad Data " + ex.getMessage());
			return false;
		}

		List<String> missingTypes = validateTypes();

		if (!missingTypes.isEmpty()) {
			String str = "Missing Types ";
			for (String s : missingTypes) {
				String mtype = "<" + s + "> ";
				str = str.concat(mtype);
			}
			ret.add(str);
			return false;
		}
		
		boolean b = updateLabels(ret);
		if (b) {
		    b = doChecks(ret);
		}
		return b;
	}
	private boolean updateLabels(List<String> ret) {
		List<NData> l = data.getData();
		for (NData n : l) {
			String label = n.getLabel();
			Csbtype ty = n.getType();

			if (TypeHelper.getObj(getUuid(), repos).getEids().contains(ty.getId()))
				n.setLabel(ty.getName());
			else {
				if (TypeHelper.getObj(getUuid(), repos).getCids().contains(ty.getId())) {
					n.setLabel(ty.getName());
				} else {
					String nstr = label.substring(ty.getName().length() + 1).trim();
					n.setLabel(nstr);
				}
			}
		}
		return true;
	}
	
	private List<String> validateTypes()
	{ 
		List<String> m = new Vector<>();
		List<Csbtype> types = repos.getCsbTypeRepository().findAll();
		Csbtype type;
		
		List<NData> l = data.getData();
		for (NData n : l) {
			type = null;
			String label = n.getLabel();
			String cstr = null;
			for (Csbtype c : types) {
				if (label.startsWith(c.getName())) {
					if (cstr == null) {
						cstr = c.getName();
						type = c;
					}
					else {
						if (c.getName().length() > cstr.length()) {
							cstr = c.getName();
							type = c;	
						}
					}
				}
			}
			if (type == null) {
				System.out.println("TYPE NOT FOUND " + label);
				if (m.isEmpty()) {
					m.add("Missing Types");
				}
				m.add(label);
			} else {
				n.setType(type);
			}
		}
		
		return m;
	}

	public boolean makeData(List<String> err)
	{
		SaveO obj = new SaveO(this, ltype, data, imdata,err, repos.getChecksRepository(), this.pp);
	
		return obj.makeData();
	}
}

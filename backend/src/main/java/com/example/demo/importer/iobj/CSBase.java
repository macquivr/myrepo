package com.example.demo.importer.iobj;

import com.example.demo.dto.ImportDTO;
import com.example.demo.importer.*;
import com.example.demo.importer.data.*;

import java.util.UUID;
import java.util.List;
import com.example.demo.utils.mydate.DUtil;
import java.util.Vector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.example.demo.domain.*;
import com.example.demo.importer.CsbEType;

public abstract class CSBase extends IBase {
	private static final Logger log = LoggerFactory.getLogger(CSBase.class);
	protected CsbEType etype = null;

	public CSBase(UUID uuid, Repos r, ImportDTO dto)
	{
		super(uuid,r, dto);
	}
	
	public boolean validateFile(List<String> ret)
	{
		try {
			new PdfData(this,data, etype).go();
		} catch (BadDataException ex) {
			ret.add("Bad Data " + ex.getMessage());
			return false;
		}
	
		List<String> missingTypes = validateTypes();
		if (missingTypes == null) {
			ret.add("Could not validate Types.");
			return false;
		}
		
		if (missingTypes.size() > 0) {
			String str = "Missing Types ";
			for (String s : missingTypes)
				str = str.concat(s + " ");
			ret.add(str);
			return false;
		}
		
		//data.Print();
		updateLabels();
		
		boolean b = doChecks(ret);
		if (!b)
			return b;

		data.getStmt().setLtype(getLtype(ltype));
		data.getStmt().setCredit(credit);
		data.getStmt().setFee(0.0);
		return true;
	}
	
	
	
	private void updateLabels()
	{
		List<NData> l = data.getData();
		for (NData n : l) {
			String label = n.getLabel();
			Csbtype ty = n.getType();
			
			String nstr = label.substring(ty.getName().length()+1).trim();
			if (nstr.isEmpty()) {
				if (TypeHelper.getObj(getUuid(),repos).getEids().contains(ty.getId()))
					n.setLabel(ty.getName());
				else 
					log.error("Bad Type? " + label);
			} else {
				if (TypeHelper.getObj(getUuid(), repos).getCids().contains(ty.getId())) {
					try {
						n.setCheck(Integer.valueOf(nstr));
						n.setLabel(ty.getName());
					} catch (Exception ex) {
						log.error("Bad check " + n.getLabel());
						break;
					}
				} else
					n.setLabel(nstr);
			} 
		}
	}
	
	private List<String> validateTypes()
	{ 
		List<String> m = new Vector<String>();
		List<Csbtype> types = repos.getCsbTypeRepository().findAll();
		Csbtype type = null;
		
		List<NData> l = data.getData();
		for (NData n : l) {
			type = null;
			String label = n.getLabel();
			String cstr = null;
			for (Csbtype c : types) {
				if (label.startsWith(c.getName())) {
					if (cstr == null) {
						cstr = c.getName();
						type = c;;
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
				if (m.size() == 0) {
					m.add("Missing Types");
				}
				m.add(label);
			} else {
				n.setType(type);
			}
		}
		
		return m;
	}

	public boolean makeData(Statements stmts, List<String> err)
	{
		if (stmts.getName() == null) {
			if (!DUtil.isValidDate(data.getSDate(),DUtil.MMDDYYYY)) {
				log.error("Bad bad " + data.getSDate());
				return false;
			}

			stmts.setName(DUtil.translate(data.getSDate(),DUtil.MMDDYYYY,DUtil.MMMYYYY));
			stmts.setStmtdate(DUtil.firstOfMonth(DUtil.getStdDate(data.getSDate())));
		}
		SaveO obj = new SaveO(this, ltype, data, imdata,err);
	
		return obj.makeData(stmts);
	}
}

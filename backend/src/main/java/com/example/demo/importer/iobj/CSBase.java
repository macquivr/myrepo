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

		if (!missingTypes.isEmpty()) {
			String str = "Missing Types ";
			for (String s : missingTypes)
				str = str.concat(s + " ");
			ret.add(str);
			return false;
		}
		
		//data.Print();
		boolean b = updateLabels(ret);
		if (!b)
			return false;

		b = doChecks(ret);
		if (!b)
			return false;

		data.getStmt().setLtype(getLtype(ltype));
		data.getStmt().setCredit(credit);
		data.getStmt().setFee(0.0);
		return true;
	}
	
	
	
	private boolean updateLabels(List<String> ret) {
		List<NData> l = data.getData();
		for (NData n : l) {
			String label = n.getLabel();
			Csbtype ty = n.getType();

			String nstr = label.substring(ty.getName().length() + 1).trim();
			if (nstr.isEmpty()) {
				if (TypeHelper.getObj(getUuid(), repos).getEids().contains(ty.getId()))
					n.setLabel(ty.getName());
				else
					log.error("Bad Type? " + label);
			} else {
				if (TypeHelper.getObj(getUuid(), repos).getCids().contains(ty.getId())) {
					try {
						n.setCheck(Integer.parseInt(nstr));
						n.setLabel(ty.getName());
						log.info("YYY Setting label " + n.getLabel());
					} catch (Exception ex) {
						ret.add("Bad Check " + n.getLabel());
						log.error("Bad check " + n.getLabel());
						return false;
					}
				} else
					n.setLabel(nstr);
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
	
		return obj.makeData();
	}
}

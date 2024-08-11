package com.example.demo.importer;

import com.example.demo.domain.*;
import java.util.List;

public interface Iimport {
	public boolean verifyFile(List<String> ret);
	public boolean validateFile(List<String> ret);
	public List<String> validateLabels(String session);
	public boolean importData(Statements stmts,boolean doSave, List<String> err);
	public void setStype(List<String> ret);
	public void setPayperiod(Payperiod pp);
}

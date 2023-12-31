package com.example.demo.utils;

import java.math.*;
import java.util.HashMap;

public class Utils {
    public static String getAkey(String key, HashMap<String,Double> map)
    {
        String akey;
        String nkey = key.substring(2);
        String wkey;
        if (key.startsWith("C "))
            wkey = "D " + nkey;
        else
            wkey = "C " + nkey;
        Double fd = map.get(wkey);
        if (fd == null)
            akey = nkey;
        else
            akey = key;

        return akey;
    }

	public static double dvAdd(double a,double b)
	{
		double tmp = a + b;
		return Utils.dv(tmp);
	}
	
	public static double dvSub(double a,double b) 
	{
		double tmp = a - b;
		return Utils.dv(tmp);
	}
	
	public static double dv(double x)
	{
		int numberofDecimals = 2;
		BigDecimal d;
	    if ( x > 0) {
	        d = new BigDecimal(String.valueOf(x)).setScale(numberofDecimals, BigDecimal.ROUND_HALF_DOWN);
	    } else {
	        d = new BigDecimal(String.valueOf(x)).setScale(numberofDecimals, BigDecimal.ROUND_HALF_UP);
	    }
	   
	    return d.doubleValue();
	}
	
	public static double convertDouble(double d)
    {
        BigDecimal bd = new BigDecimal(d).setScale(2, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
	
	public static String itrim(String str)
	{
		if (str == null)
			return null;
		String ret = str;
		while (ret.contains("  ")) {
			ret = ret.replaceAll("  ", " ");
			if (ret.isEmpty()) 
				return ret;
		}
		return ret;
	}
	
	public static Double dval(String dstr) {
		try {
			return Double.valueOf(dstr.replaceAll("\\$", "").replaceAll(",", ""));
		} catch (NumberFormatException ex) {
			return null;
		}
	}
}

package com.example.demo.utils;

import com.example.demo.state.Consolidate;

import java.time.LocalDate;

public class ConsolidateUtils {

    public static boolean isYearly(Consolidate type) {
        return ((type == Consolidate.YEARLY) || isSpecificMonth(type) || isSpecificQuarter(type));
    }
    public static String DStr(Consolidate type, LocalDate dt) {
        String ret = null;
        if ((type == null) || (type == Consolidate.NONE)) {
            return null;
        }
        int year = dt.getYear();
        String ystr = " " + year;
        if (isSpecificMonth(type)) {
            switch (type) {
                case JANUARY:
                    ret = "Jan" + ystr;
                    break;
                case FEBRUARY:
                    ret = "Feb" + ystr;
                    break;
                case MARCH:
                    ret = "Mar" + ystr;
                    break;
                case APRIL:
                    ret = "Apr" + ystr;
                    break;
                case MAY:
                    ret = "May" + ystr;
                    break;
                case JUNE:
                    ret = "Jun" + ystr;
                    break;
                case JULY:
                    ret = "Jul" + ystr;
                    break;
                case AUGUST:
                    ret = "Aug" + ystr;
                    break;
                case SEPTEMBER:
                    ret = "Sep" + ystr;
                    break;
                case OCTOBER:
                    ret = "Oct" + ystr;
                    break;
                case NOVEMBER:
                    ret = "Nov" + ystr;
                    break;
                case DECEMBER:
                    ret = "Dec" + ystr;
                    break;
                default:
                    ret = "EEK!";
            }
        }
        if (isSpecificQuarter(type)) {
            switch(type) {
                case Q1:
                    ret = "Q1" + ystr;
                    break;
                case Q2:
                    ret = "Q2" + ystr;
                    break;
                case Q3:
                    ret = "Q3" + ystr;
                    break;
                case Q4:
                    ret = "Q4" + ystr;
            }
        }
        return ret;
    }

    public static boolean isSpecificQuarter(Consolidate type) {
        return ((type == Consolidate.Q1) ||
                (type == Consolidate.Q2) ||
                (type == Consolidate.Q3) ||
                (type == Consolidate.Q4));
    }
    public static boolean isSpecificMonth(Consolidate type) {
        return ((type == Consolidate.JANUARY) ||
                (type == Consolidate.FEBRUARY) ||
                (type == Consolidate.MARCH) ||
                (type == Consolidate.APRIL) ||
                (type == Consolidate.MAY) ||
                (type == Consolidate.JUNE) ||
                (type == Consolidate.JULY) ||
                (type == Consolidate.AUGUST) ||
                (type == Consolidate.SEPTEMBER) ||
                (type == Consolidate.OCTOBER) ||
                (type == Consolidate.NOVEMBER) ||
                (type == Consolidate.DECEMBER));
    }
}

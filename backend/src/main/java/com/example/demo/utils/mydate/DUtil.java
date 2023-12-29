package com.example.demo.utils.mydate;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DUtil {
    public static String FULL_FMT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
    public static String MMDD = "MM/dd";
    public static String MMMYYYY = "MMM yyyy";
    public static String MMDDYYYY = "MM/dd/yyyy";
    public static String MMDDYY = "MM/dd/yy";
    private static DateTimeFormatter getDFmt(String fmt) { return DateTimeFormatter.ofPattern(fmt); }

    public static boolean isValidDate(String dstr, String fmt) {
        DateTimeFormatter dfmt = getDFmt(fmt);

        try {
            dfmt.parse(dstr);
        } catch (Exception ex) {
            return false;
        }
        return true;
    }

    public static LocalDate getDate(String dstr, String fmt) {
        if (!isValidDate(dstr,fmt))
            return null;

        return LocalDate.parse(dstr,getDFmt(fmt));
    }

    public static String getDate(LocalDate date, String fmt) {
        DateTimeFormatter dfmt = getDFmt(fmt);

        return dfmt.format(date);
    }

    public static boolean isValidMMDD(String dstr) {
        return isValidDate(dstr, DUtil.MMDD);
    }

    public static String getDefaultYear()
    {
        LocalDate d = stopNow();
        return getDFmt("YYYY").format(d);
    }

    public static LocalDate startNow() {
        LocalDate date = LocalDate.now();
        date = date.minusMonths(1);
        return date.withDayOfMonth(1);
    }

    public static LocalDate stopNow() { return LocalDate.now(); }

    public static boolean isEqualToOrAfter(LocalDate src, LocalDate target) {
        return (src.isAfter(target) || src.isEqual(target));
    }

    public static LocalDate getStdDate(String dstr) { return getDate(dstr,MMDDYYYY); }
    public static LocalDate first() { return getStdDate("01/01/2012"); }

    public static String monthMinusOne(String dstr) {
        LocalDate dt = getDate(dstr, MMDDYY);
        dt = dt.minusMonths(1);
        return dt.format(getDFmt(MMDDYY));
    }

    public static LocalDate lastOfYear(LocalDate d) { return d.withDayOfYear(d.lengthOfYear()); }
    public static LocalDate firstOfYear(LocalDate d) { return d.withDayOfYear(1); }
    public static LocalDate lastOfMonth(LocalDate d) { return d.withDayOfMonth(d.lengthOfMonth()); }
    public static LocalDate firstOfMonth(LocalDate d) { return d.withDayOfMonth(1); }
    public static LocalDate setMonth(LocalDate d, int month) { return d.withMonth(month); }
    public static String translate(String dstr, String fmtFrom, String fmtTo)
    {
        DateTimeFormatter from = getDFmt(fmtFrom);
        DateTimeFormatter to = getDFmt(fmtTo);

        LocalDate date = null;
        try {
            date = LocalDate.parse(dstr,from);
        } catch (Exception ex) {
            return null;
        }

        return to.format(date);
    }
}
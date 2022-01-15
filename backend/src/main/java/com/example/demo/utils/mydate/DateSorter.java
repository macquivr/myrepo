package com.example.demo.utils.mydate;

import java.time.LocalDate;
import com.example.demo.utils.uidata.Idate;
import java.util.Comparator;

public class DateSorter implements Comparator<Idate> {

    @Override
    public int compare(Idate idate, Idate t1) {
        LocalDate d1 = idate.getDate();
        LocalDate d2 = t1.getDate();

        if (d1.isBefore(d2))
            return -1;
        if (d1.isAfter(d2))
            return 1;
        return 0;
    }
}

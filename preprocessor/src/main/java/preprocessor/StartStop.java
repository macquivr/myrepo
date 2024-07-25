package preprocessor;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class StartStop {
    private final String DFMT1 = "MM/dd/yyyy";
    private final String DFMT2 = "yyyy-MM-dd";


    private final LocalDate start;
    private final LocalDate stop;

    public StartStop(String d1, String d2) {
        this.start = makeDate(d1,DFMT1);
        this.stop = makeDate(d2,DFMT1);
    }

    public boolean isValid() {
        return ((this.start != null) && (this.stop != null));
    }

    public boolean inRange(String dstr) {
        LocalDate d = makeDate(dstr.trim(), DFMT1);
        if (d == null) {
            d = makeDate(dstr.trim(), DFMT2);
            if (d == null) {
                System.out.println("NOT VALID DATE " + dstr);
                return false;
            }
        }
        boolean before = d.isBefore(this.start);
        boolean after = d.isAfter(this.stop);
        return (!before && !after);
    }

    public String getStart() {
        DateTimeFormatter dfmt = DateTimeFormatter.ofPattern(DFMT1);

        return start.format(dfmt);
    }

    public String getStop() {
        DateTimeFormatter dfmt = DateTimeFormatter.ofPattern(DFMT1);

        return stop.format(dfmt);
    }

    private LocalDate makeDate(String dstr, String fmt) {
       DateTimeFormatter dfmt = DateTimeFormatter.ofPattern(fmt);

       try {
           dfmt.parse(dstr);
       } catch (Exception ex) {

            return null;
        }

        return LocalDate.parse(dstr,dfmt);
   }
}

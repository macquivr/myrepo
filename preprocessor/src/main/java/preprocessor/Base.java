package preprocessor;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public abstract class Base {
    protected StartStop dates;
    protected String fname = null;
    protected List<String> ilines = null;
    protected List<String> olines = null;

    public abstract void doTransform();
    public abstract boolean doIn();

    public Base(StartStop d) {
        this.dates = d;
        ilines = new ArrayList<String>();
        olines = new ArrayList<String>();
    }

    public boolean go() {
        try {
            if (doIn()) {
                doTransform();
            }
            doOut();
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
        return true;
    }

    protected boolean checkRange(String str, int idx) {
        String[] data = str.split(",");
        String dstr = data[idx];

        return this.dates.inRange(dstr);
    }

    private void  doOut() throws Exception {
        if (olines.isEmpty()) {
            olines.add("EMPTY");
        }
        String fileName = System.getenv("W_DATA_DIR")  + "/" + fname;

        String str = "";
        for (String s : olines) {
            str = str.concat(s) + "\n";
        }
        File myObj = new File(fileName);
        myObj.delete();
        myObj.createNewFile();
        Files.write(Paths.get(fileName),str.getBytes(), StandardOpenOption.WRITE);
    }

    private String sanitize(String str) {
        String ret = sanitize1(str);
        return sanitize2(ret);
    }

    private String sanitize1(String str) {
        String ret = "";
        boolean on = false;
        int len = str.length();
        int i = 0;
        int n = 0;
        for (i=0;i<len;i++) {
            char c = str.charAt(i);
            if (c == '"') {
                on = !on;
                if (!on) {
                    if (n == 0) {
                        ret = ret.concat("X");
                    } else {
                        n = 0;
                    }
                }
            } else {
                if (on) {
                    n++;
                }
                if (!on || (c != ',')) {
                    ret = ret.concat(String.valueOf(c));
                }
            }
        }

        return ret;
    }

    private String sanitize2(String str) {

        String ret = "";
        boolean on = false;
        int len = str.length();
        int i = 0;
        for (i=0;i<len;i++) {
            char c = str.charAt(i);
            ret = ret.concat(String.valueOf(c));
            if (i != (len-1)) {
                char c2 = str.charAt(i + 1);
                if ((c == ',') && (c2 == ',')) {
                    ret = ret.concat("0");
                }
            }
        }

        return ret;
    }

    protected boolean doInCsv()  {
        try {
            String fileName = System.getenv("WS_DATA_DIR") + "/" + fname;
            String str = new String(Files.readAllBytes(Paths.get(fileName)));
            StringTokenizer st = new StringTokenizer(str, "\n");
            while (st.hasMoreTokens()) {
                String l = st.nextToken();
                if (l.equals("EMPTY")) {
                    olines.add("EMPTY");
                    return false;
                }
                ilines.add(sanitize(l));
            }
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

}

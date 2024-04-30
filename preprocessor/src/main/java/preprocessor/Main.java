package preprocessor;

import preprocessor.impl.*;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("Requires data.");
            return;
        }
        Main m = new Main();
        m.go(args);
    }

    private void go(String[] args) {
        List<Base> objs = new ArrayList<Base>();
        StartStop d = new StartStop(args[0],args[1]);
        if (!d.isValid()) {
            System.out.println("Invalid dates " + args[0] + " " + args[1]);
            return;
        }
        populate(objs,d);
        for (Base b : objs) {
            if (!b.go())
                return;
        }
    }

    private void populate(List<Base> objs, StartStop d) {
        Base aaa = new AaaImpl(d);
        Base amazon = new AmazonImpl(d);
        Base capone = new CaponeImpl(d);
        Base usaa = new UsaaImpl(d);
        Base ml = new MLImpl(d);

        Base main = new MainCsbImpl(d);
        Base slush = new SlushCsbImpl(d);
        Base annual = new AnnualCsbImpl(d);

        objs.add(aaa);
        objs.add(amazon);
        objs.add(capone);
        objs.add(usaa);
        objs.add(ml);
        objs.add(main);
        objs.add(slush);
        objs.add(annual);
    }
}

package com.example.demo.dto;

import com.example.demo.domain.Label;
import com.example.demo.domain.Names;
import com.example.demo.domain.Location;
import com.example.demo.domain.Category;
import com.example.demo.misc.Nbucket;

import java.util.List;
import java.util.Vector;

public class JustNameDTO {
    private List<Nbucket> data;

    public JustNameDTO(List dt)
    {
        data = new Vector<Nbucket>();
        for (Object l : dt) {
            Nbucket n = new Nbucket();
            String nm = null;
            if (l instanceof Label) {
                Label lbl = (Label) l;
                nm = lbl.getName();
            }
            if (l instanceof Names) {
                Names nmo = (Names) l;
                nm = nmo.getName();
            }
            if (l instanceof Category) {
                Category nmo = (Category) l;
                nm = nmo.getName();
            }
            if (l instanceof Location) {
                Location nmo = (Location) l;
                nm = nmo.getName();
            }

            if (nm != null) {
                n.setName(nm);
                data.add(n);
            }
        }
    }

    public List<Nbucket> getData() { return data; }
    public void setLabel(List<Nbucket> d) { data = d; }
}

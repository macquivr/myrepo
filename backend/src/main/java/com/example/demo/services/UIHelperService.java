package com.example.demo.services;

import com.example.demo.dto.ui.UIKeyValueDTO;
import com.example.demo.repository.PayeeRepository;
import com.example.demo.repository.StypeRepository;
import com.example.demo.domain.Payee;
import com.example.demo.domain.Stype;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Vector;

@Service
public class UIHelperService {

    @Autowired
    PayeeRepository payeeRepository;

    @Autowired
    StypeRepository stypeRepository;

    public PayeeRepository getPayeeRepo() { return payeeRepository; }

    public List<UIKeyValueDTO> getPayees() {
        List<Payee> payees = payeeRepository.findAll();
        List<UIKeyValueDTO> ret = new Vector<>();

        for (Payee p : payees) {
            UIKeyValueDTO data = new UIKeyValueDTO();

            data.setValue(String.valueOf(p.getId()));
            data.setLabel(p.getName());

            ret.add(data);
        }

        return ret;
    }

    public List<UIKeyValueDTO> getStypes() {
        List<Stype> stypes = stypeRepository.findAll();
        List<UIKeyValueDTO> ret = new Vector<>();

        for (Stype p : stypes) {
            UIKeyValueDTO data = new UIKeyValueDTO();

            data.setValue(String.valueOf(p.getId()));
            data.setLabel(p.getName());

            ret.add(data);
        }

        return ret;
    }
}

package com.example.demo.controllers;

import com.example.demo.domain.Transfer;
import com.example.demo.dto.TransferDTO;
import com.example.demo.dto.TransferTableDTO;
import com.example.demo.dto.TransferRowDTO;
import com.example.demo.services.TransferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/")
public class TransferController {

    @Autowired
    private TransferService service;

    @GetMapping(value = "/Transfer", produces = "application/json")
    public @ResponseBody
    TransferDTO getTransfer() {
        List<Transfer> data = service.findAll();
        return new TransferDTO(data);
    }

    @GetMapping(value = "/transfertable", produces = "application/json")
    public @ResponseBody
    TransferTableDTO getTransferTable() {
        List<Transfer> data = service.findAll();

        List<TransferRowDTO> tdata = data.stream().map(TransferRowDTO::new).collect(Collectors.toList());

        return new TransferTableDTO(tdata);
    }
}

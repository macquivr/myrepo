package com.example.demo.reports;

import com.example.demo.dto.SessionDTO;

import java.io.*;

public interface ReportI {
    void go(FileWriter w, SessionDTO session) throws Exception ;
}

package com.example.demo.reports;

import com.example.demo.dto.SessionDTO;

import java.io.*;
import java.util.List;

public interface ReportI {
    String go(FileWriter w, SessionDTO session) throws Exception ;
}

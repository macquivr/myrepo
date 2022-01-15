package com.example.demo.dto.ui;

import java.util.List;

public class UtilsTableDTO {
    private List<UtilsRowDTO> utils;

    public UtilsTableDTO() { /* nop */ }

    public UtilsTableDTO(List<UtilsRowDTO> dt) {
        utils = dt;
    }

    public List<UtilsRowDTO> getUtils() { return utils; }
    public void setUtils(List<UtilsRowDTO> d) { utils = d; }
}

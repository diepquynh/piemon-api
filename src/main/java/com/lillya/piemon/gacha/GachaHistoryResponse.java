package com.lillya.piemon.gacha;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
class GachaHistoryData {
    private byte page;
    private byte size;
    private byte total;
    private List<GachaItem> list;
    private String region;

    public GachaHistoryData() {
        this.list = new ArrayList<>();
    }
}

@Data
public class GachaHistoryResponse {
    private int retcode;
    private String message;
    private GachaHistoryData data;
}

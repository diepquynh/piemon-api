package com.lillya.piemon.gacha;

import lombok.Data;

@Data
public class GachaImportResponse {
    private int retcode;
    private String msg;

    public GachaImportResponse(int retcode, String msg) {
        this.retcode = retcode;
        this.msg = msg;
    }
}

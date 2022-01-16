package com.lillya.piemon.gacha;

import lombok.Data;

@Data
public class GachaType {
    private int id;
    private String name;

    public GachaType(int id, String name) {
        this.id = id;
        this.name = name;
    }
}

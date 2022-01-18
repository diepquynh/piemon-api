package com.lillya.piemon.gacha.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.persistence.*;
import java.sql.Timestamp;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "gacha_item")
@Entity
public class GachaItem {

    @Id
    private Long id;

    private Integer uid;

    @Column(length = 10)
    private String item_id;

    private Byte count;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private Timestamp time;

    @Column(nullable = false)
    private String name;

    @Column(length = 5, nullable = false)
    private String lang;

    @Column(length = 15, nullable = false)
    private String item_type;

    private Byte rank_type;

    @Column(name = "gacha_type")
    @JsonProperty("gacha_type")
    private Short gachaType;
}

package com.lillya.piemon.gacha;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
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

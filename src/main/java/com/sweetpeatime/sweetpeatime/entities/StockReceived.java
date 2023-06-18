package com.sweetpeatime.sweetpeatime.entities;

import java.util.Date;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Stock")
public class StockReceived {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Integer flowerId;
    private Integer quantity;
    private String unit;

    @JsonFormat(pattern="yyyy-MM-dd", timezone="Asia/Bangkok")
    private Date lot;
    private Integer flowerPriceId;
    private Integer floristId;
    private Integer deleteQty;

}

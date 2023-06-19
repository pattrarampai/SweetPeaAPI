package com.sweetpeatime.sweetpeatime.entities;

import lombok.*;

import java.util.Date;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name = "PurchaseOrderDetail")
public class PurchaseOrderDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Integer flowerId;
    private Integer quantity;
    private Integer packId;
    private Integer supplierId;
    private Integer priceId;
    @JsonFormat(pattern="yyyy-MM-dd", timezone="Asia/Bangkok")
    private Date lot;
    private Integer floristId;
    private Integer receivedQty;
    private Double price;
    private Double weight;
    private String vehicle; // BIKE, CAR
    private Double transportationFee;// >> 300,300

//    private Double // transportation > เอาน้ำหนักรวมของ 1 ช่าง > หาว่าจะส่งโดยมอเตอร์ไซต์หรือรถยนต์ตามน้ำหนักรวม >
//    น้ำหนักรวม <= 10 >> มอเตอร์ไซต์
//    น้ำหนักรวม > 10 >> รถยนต์

//    BIKE > 150
//    CAR > 300



//    @JsonBackReference
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "purchaseOrderId")
//private PurchaseOrder purchaseOrder;
    private Integer purchaseOrderId;
    
}

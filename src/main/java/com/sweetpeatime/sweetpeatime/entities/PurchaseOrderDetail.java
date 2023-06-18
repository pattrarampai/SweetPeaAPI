package com.sweetpeatime.sweetpeatime.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
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

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "purchaseOrderId")
    private PurchaseOrder purchaseOrder;
    
}

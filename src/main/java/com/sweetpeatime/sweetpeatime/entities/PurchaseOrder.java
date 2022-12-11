package com.sweetpeatime.sweetpeatime.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;
import java.util.List;

import javax.persistence.*;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "PurchaseOrder")
public class PurchaseOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @JsonFormat(pattern="yyyy-MM-dd", timezone="Asia/Bangkok")
    private Date date;
    private String status;
    private Double total;
    private Double transportationFee;

    // @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    // @JoinColumn(name = "PurchaseOrderId")
    // private List<PurchaseOrderDetail> purchaseOrderDetail;
    
}

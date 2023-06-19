package com.sweetpeatime.sweetpeatime.entities;

import lombok.*;

import java.util.Date;
import java.util.List;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name = "PurchaseOrder")
public class PurchaseOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @JsonFormat(pattern="yyyy-MM-dd", timezone="Asia/Bangkok")
    private Date date; //orderDate
    private String status;
    private Double total;
    private Double transportationFee;

    @JsonManagedReference
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "purchaseOrderId", referencedColumnName = "id")
    private List<PurchaseOrderDetail> purchaseOrderDetail;
//    private Integer purchaseOrderId;
}

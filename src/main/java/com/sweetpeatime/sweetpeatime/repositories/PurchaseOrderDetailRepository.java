package com.sweetpeatime.sweetpeatime.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sweetpeatime.sweetpeatime.entities.PurchaseOrderDetail;

@Repository
public interface PurchaseOrderDetailRepository extends JpaRepository<PurchaseOrderDetail, Integer> {

    List<PurchaseOrderDetail> findByPurchaseOrderIdAndFloristIdAndFlowerId(Integer id, Integer floristId, Integer flowerId);
    List<PurchaseOrderDetail> findByPurchaseOrderId(Integer id);
}

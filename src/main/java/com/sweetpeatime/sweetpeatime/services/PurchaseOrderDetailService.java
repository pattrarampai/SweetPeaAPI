package com.sweetpeatime.sweetpeatime.services;

import com.sweetpeatime.sweetpeatime.entities.PurchaseOrderDetail;
import com.sweetpeatime.sweetpeatime.repositories.PurchaseOrderDetailRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class PurchaseOrderDetailService {
    @Autowired
    private PurchaseOrderDetailRepository purchaseOrderDetailRepository;
    public List<PurchaseOrderDetail> findByPurchaseOrderIdAndFloristIdAndFlowerId(Integer poId, Integer floristId, Integer flowerId){
        return purchaseOrderDetailRepository.findByPurchaseOrderIdAndFloristIdAndFlowerId(poId, floristId, flowerId);
    }
    public PurchaseOrderDetail save(PurchaseOrderDetail purchaseOrderDetail){
        return purchaseOrderDetailRepository.save(purchaseOrderDetail);
    }
    public void deleteAllById(List<Integer> ids){
        purchaseOrderDetailRepository.deleteAllById(ids);
    }
}

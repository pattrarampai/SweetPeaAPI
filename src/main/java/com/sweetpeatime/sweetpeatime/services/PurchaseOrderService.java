package com.sweetpeatime.sweetpeatime.services;

import com.sweetpeatime.sweetpeatime.common.exception.CommonException;
import com.sweetpeatime.sweetpeatime.entities.PurchaseOrder;
import com.sweetpeatime.sweetpeatime.repositories.PurchaseOrderRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.sweetpeatime.sweetpeatime.common.constant.CommonConstant;
import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class PurchaseOrderService {
    @Autowired
    private PurchaseOrderRepository purchaseOrderRepository;
    public PurchaseOrder findById(Integer id){
        return purchaseOrderRepository.findById(id)
                .orElseThrow(() -> new CommonException("Your Purchase Order can't be found"));
    }
    public void confirmOrder(Integer id){
        var purchaseOrder = findById(id);
        if (!purchaseOrder.getStatus().equals(CommonConstant.PO_STATUS_DRAFT)) {
            throw new CommonException("Your Purchase Order is Invalid");
        }
        purchaseOrder.setStatus(CommonConstant.PO_STATUS_CONFIRM);
        purchaseOrderRepository.save(purchaseOrder);
    }
    public List<PurchaseOrder> findByStatusAndOrderDate(String status, Date orderDate){
        return purchaseOrderRepository.findByStatusAndDate(status, orderDate);
    }
    public PurchaseOrder create(PurchaseOrder po){
        return purchaseOrderRepository.save(po);
    }

    public List<PurchaseOrder> findByStatus(String status){
        return purchaseOrderRepository.findByStatus(status);
    }
}

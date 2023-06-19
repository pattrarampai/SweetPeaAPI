package com.sweetpeatime.sweetpeatime.services;

import com.sweetpeatime.sweetpeatime.common.exception.CommonException;
import com.sweetpeatime.sweetpeatime.entities.PurchaseOrder;
import com.sweetpeatime.sweetpeatime.entities.SalesOrder;
import com.sweetpeatime.sweetpeatime.repositories.SalesOrderRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class SalesOrderService {
    @Autowired
    private SalesOrderRepository salesOrderRepository;
    public SalesOrder findById(Integer id){
        return salesOrderRepository.findById(id).orElseThrow(() -> new CommonException("Your Sales Order can't be found"));
    }

    public List<SalesOrder> findByStatusOrderByDeliveryDateTimeAsc(String status){
        return salesOrderRepository.findByStatusOrderByDeliveryDateTimeAsc(status);
    }

}

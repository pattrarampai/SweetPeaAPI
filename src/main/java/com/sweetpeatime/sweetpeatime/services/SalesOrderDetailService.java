package com.sweetpeatime.sweetpeatime.services;

import com.sweetpeatime.sweetpeatime.entities.SalesOrderDetail;
import com.sweetpeatime.sweetpeatime.repositories.SalesOrderDetailRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class SalesOrderDetailService {
    @Autowired
    private SalesOrderDetailRepository salesOrderDetailRepository;
    public List<SalesOrderDetail> findAllBySalesOrderId(Integer id){
        return salesOrderDetailRepository.findAllBySalesOrderId(id);
    }

    public List<SalesOrderDetail> findByStatus(String status){
        return salesOrderDetailRepository.findByStatus(status);
    }

    public void deleteAllById(List<Integer> ids){
        salesOrderDetailRepository.deleteAllById(ids);
//        return salesOrderDetailRepository.findByStatus(status);
    }

}

package com.sweetpeatime.sweetpeatime.services;

import com.sweetpeatime.sweetpeatime.common.utils.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.CollectionUtils;

import java.text.ParseException;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Slf4j
class PurchaseOrderServiceTest {

    @Autowired
    private PurchaseOrderService purchaseOrderService;
    @Test
    void findByStatusAndOrderDate() throws ParseException {
        var test = purchaseOrderService.findByStatusAndOrderDate("DRAFT", DateUtil.getDate("20/05/2023","dd/MM/yyyy"));
        log.info("{}",test.size());
        if(CollectionUtils.isEmpty(test)){
            log.info("empty");
        }else{
            log.info("not empty");
        }

    }
}
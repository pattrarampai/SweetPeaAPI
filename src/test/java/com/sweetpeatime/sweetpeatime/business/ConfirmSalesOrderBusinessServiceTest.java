package com.sweetpeatime.sweetpeatime.business;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Slf4j
class ConfirmSalesOrderBusinessServiceTest {
    @Autowired
    private ConfirmSalesOrderBusinessService confirmSalesOrderBusinessService;
    @Test
    void confirmOrder() {
        confirmSalesOrderBusinessService.confirmOrder();
    }

    @Test
    void getLotDate(){
        var result = confirmSalesOrderBusinessService.getLotDate(LocalDate.now(), 1, 10);
        log.info("date: {}", result);
    }
}
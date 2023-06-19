package com.sweetpeatime.sweetpeatime.business;

import com.sweetpeatime.sweetpeatime.dto.auto.CalculateSaleOrderDetailDto;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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
        var result = confirmSalesOrderBusinessService.getLotDate(LocalDate.now().plusDays(10), 1);
        log.info("result date: {}", result);
    }

    @Test
    void addCalculateSaleOrderDetailDto() {
        List<CalculateSaleOrderDetailDto> calculateSaleOrderDetailsDto = new ArrayList();
        var item1 = CalculateSaleOrderDetailDto.builder()
                .lotDate(LocalDate.now())
                .floristId(1)
                .flowerId(1)
                .liftTime(10 - 1)
                .isStock(false)
                .quantity(10)
                .build();
        var item2 = CalculateSaleOrderDetailDto.builder()
                .lotDate(LocalDate.now())
                .floristId(1)
                .flowerId(1)
                .liftTime(10 - 1)
                .isStock(false)
                .quantity(20)
                .build();
        ConfirmSalesOrderBusinessService.addCalculateSaleOrderDetailDto(calculateSaleOrderDetailsDto, item1);
        ConfirmSalesOrderBusinessService.addCalculateSaleOrderDetailDto(calculateSaleOrderDetailsDto, item2);
        log.info("size: {}", calculateSaleOrderDetailsDto);
        log.info("quantity: {}", calculateSaleOrderDetailsDto.get(0).getQuantity());
    }

    @Test
    void calculatePack(){
        var item1 = CalculateSaleOrderDetailDto.builder()
                .lotDate(LocalDate.now())
                .floristId(1)
                .flowerId(27)
                .liftTime(10 - 1)
                .isStock(false)
                .quantity(11)
                .build();
        log.info("saleOrder qty: {}", item1.getQuantity());
        var result = confirmSalesOrderBusinessService.calculatePack(item1);
        result.forEach((item) -> {
            log.info("key: {}, UnitPack: {}, qty: {}",item.getPack().getId(),item.getPack().getUnitPack(), item.getQuantity());
        });

    }

}
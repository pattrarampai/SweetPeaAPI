package com.sweetpeatime.sweetpeatime.business;

import com.sweetpeatime.sweetpeatime.common.constant.CommonConstant;
import com.sweetpeatime.sweetpeatime.common.utils.DateUtil;
import com.sweetpeatime.sweetpeatime.entities.dto.auto.CalculatePurchaseOrderItemDto;
import com.sweetpeatime.sweetpeatime.entities.Flower;
import com.sweetpeatime.sweetpeatime.entities.FlowerFormula;
import com.sweetpeatime.sweetpeatime.entities.PurchaseOrder;
import com.sweetpeatime.sweetpeatime.entities.SalesOrderDetail;
import com.sweetpeatime.sweetpeatime.services.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class ConfirmSalesOrderBusinessService2 {
    private final PurchaseOrderService purchaseOrderService;
    private final SalesOrderService salesOrderService;
    private final SalesOrderDetailService salesOrderDetailService;
    private final FlowerFormulaDetailService flowerFormulaDetailService;
    private final PackService packService;

    public void confirmOrder(Integer id){
        var confirmedSalesOrder = salesOrderService.findById(id);//1.
        var confirmedSalesOrderDetails = salesOrderDetailService.findAllBySalesOrderId(id);//1.
        var formulas = new ArrayList<FlowerFormula>();//2.
        if(!CollectionUtils.isEmpty(confirmedSalesOrderDetails)){
            for(SalesOrderDetail salesOrderDetail : confirmedSalesOrderDetails){
                formulas.add(salesOrderDetail.getFlowerFormula());
                var formulaDetails = flowerFormulaDetailService.findAllByFlowerFormulaId(salesOrderDetail.getFlowerFormula().getId());
            }
        }
        var nearlyPreparationDate = getNearlyPreparationDate(confirmedSalesOrderDetails);
        var calculatePurchaseOrderItemsDto = getCalculatePurchaseOrderItemDto(confirmedSalesOrderDetails);
        for(var calculatePurchaseOrderItemDto : calculatePurchaseOrderItemsDto){
            var flower = calculatePurchaseOrderItemDto.getFlower();
            if(flower.getIsStock()){ // case stock
                processStockFlower(calculatePurchaseOrderItemDto);
            }else{ // case not stock
                processNotStockFlower(calculatePurchaseOrderItemDto);
            }
        }
    }
    public LocalDate getNearlyPreparationDate(List<SalesOrderDetail> salesOrderDetails){
        var resultDate = LocalDate.now();
        // TODO 3.
        return resultDate;
    }
    public List<CalculatePurchaseOrderItemDto> getCalculatePurchaseOrderItemDto(List<SalesOrderDetail> salesOrderDetails){
        if(CollectionUtils.isEmpty(salesOrderDetails)){
           return new ArrayList<>();
        }
        var resultMap = new HashMap<Integer, CalculatePurchaseOrderItemDto>();
        // calculate total used flower id >> order-detail-qty * formula-detail-qty
        for(var salesOrderDetail : salesOrderDetails){
            var orderQty = salesOrderDetail.getQuantity();
            var formulaDetails = flowerFormulaDetailService.findAllByFlowerFormulaId(salesOrderDetail.getFlowerFormula().getId());
            if(CollectionUtils.isEmpty(formulaDetails)){
                continue;
            }
            for(var formulaDetail: formulaDetails){
                var flower = formulaDetail.getFlower();
                var usedFlowerQty = orderQty * formulaDetail.getQuantity();
                if(resultMap.containsKey(flower.getId())){
                    resultMap.get(flower.getId()).addQty(usedFlowerQty);
                }else{
                    resultMap.put(flower.getId(),
                            CalculatePurchaseOrderItemDto.builder()
                                .flower(flower)
                                .totalQty(usedFlowerQty)
                                .build());
                }
            }
        }
        return resultMap.values().stream()
                .collect(Collectors.toList());
    }
    public void processStockFlower(CalculatePurchaseOrderItemDto item){ // 7

    }
    public void processNotStockFlower(CalculatePurchaseOrderItemDto item){ // 7
        var lotDate = getLot(item.getFlower());
        var draftPurchaseOrder = getDraftPurchaseOrder(lotDate);
        if(CollectionUtils.isEmpty(draftPurchaseOrder)){ // draft PO is not exists

        }else{ // draft PO is exists

        }
    }
    public LocalDate getLot(Flower flower){ // 7B
        return LocalDate.now();
    }
    public List<PurchaseOrder> getDraftPurchaseOrder(LocalDate orderDate){ //8B
        return purchaseOrderService.findByStatusAndOrderDate(CommonConstant.PO_STATUS_DRAFT, DateUtil.getDate(orderDate));
    }
//    public Object getPack(Flower flower){
//        // get pack by flowerId order by unitPack
//        return packService.findByFlowId(flower.getId());
//
//    }




}

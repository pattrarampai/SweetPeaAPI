package com.sweetpeatime.sweetpeatime.business;

import com.sweetpeatime.sweetpeatime.common.constant.CommonConstant;
import com.sweetpeatime.sweetpeatime.common.exception.CommonException;
import com.sweetpeatime.sweetpeatime.common.utils.DateUtil;
import com.sweetpeatime.sweetpeatime.dto.auto.CalculateSaleOrderDetailDto;
import com.sweetpeatime.sweetpeatime.entities.*;
import com.sweetpeatime.sweetpeatime.services.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.sql.Array;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class ConfirmSalesOrderBusinessService {
    private final PurchaseOrderService purchaseOrderService;
    private final SalesOrderService salesOrderService;
    private final SalesOrderDetailService salesOrderDetailService;
    private final FlowerFormulaDetailService flowerFormulaDetailService;
    private final PackService packService;
    private final PurchaseOrderDetailService purchaseOrderDetailService;
    private final LotService lotService;

    // ORDER >> DRAFT > CONFIRM >> ไม่ใช่ draft >> COMPLETED



    public void confirmOrder(){
        // select ORDER where status = 'CONFIRM';
        List<SalesOrder> confirmedSaleOrderItems = salesOrderService.findByStatusOrderByDeliveryDateTimeAsc(CommonConstant.SALE_ORDER_STATUS_CONFIRM);
        if(CollectionUtils.isEmpty(confirmedSaleOrderItems)){
            return;
        }
        SalesOrder firstSalesOrder = confirmedSaleOrderItems.get(0);
        LocalDate saleOrderDeliveryDate = getSaleOrderDeliveryDate(firstSalesOrder.getDeliveryDateTime());
        log.info("saleOrderDeliveryDate: {}", saleOrderDeliveryDate);// ต้องดึงของแต่ florist หรือไม่
        List<SalesOrderDetail> saleOrderDetails = salesOrderDetailService.findByStatus(CommonConstant.SALE_ORDER_STATUS_CONFIRM);
        // get saleOrder detail ออกมาทั้งหมด ตามที่ CONFIRM แล้ว >> floristId >> order by florist
        log.info("{}", saleOrderDetails.size());

        List<CalculateSaleOrderDetailDto> calculateSaleOrderDetailsDto = new ArrayList();

        for(SalesOrderDetail saleOrderDetail: saleOrderDetails){
            var saleOrder = saleOrderDetail.getSalesOrder();
            var deliveryDate = DateUtil.toLocalDate(saleOrder.getDeliveryDateTime());
            var formulaDetails = flowerFormulaDetailService.findAllByFlowerFormulaId(saleOrderDetail.getFlowerFormula().getId());
            for(var formulaDetail: formulaDetails){
                var floristId = saleOrderDetail.getFlorist().getId();
                var flower = formulaDetail.getFlower();
                var lotDate = getLotDate(deliveryDate, flower.getLotNo(), flower.getLifeTime());
                var qty = saleOrderDetail.getQuantity() * formulaDetail.getQuantity();
                addCalculateSaleOrderDetailDto(calculateSaleOrderDetailsDto,
                        CalculateSaleOrderDetailDto.builder()
                                .deliveryDate(deliveryDate)
                                .lotDate(lotDate)
                                .floristId(floristId)
                                .flowerId(flower.getId())
                                .liftTime(flower.getLifeTime() - 1)
                                .isStock(flower.getIsStock())
                                .quantity(qty)
                        .build());
            }
        }










//        sale-order-detail.qty * (flower-formular-detail.flower-id * flower-formular-detail.qty)
//        >> จะได้รายการดอกไม้ กับจำนวน ที่ต้องสั่ง ของแต่ละ florist
        // คำนวณอายุดอกไม้ของแต่ florist >> flower.lifttime -1 ของทุกตัว
//        florist
//            list
//                flower-id
//                qty
//                lifetime หาได้จาก flower.lifetime -1 >> ใช้เป็นตัวเลข







//        >> stock หรือ ไม่ stock ดูที่ flower>> ทำทีละ florist

//        ไม่ stock : 0


        // get sale order detail by confirmedSaleOrderItems

        // group by lotDate, floristId, flowerId, sum(qty)


        // select * from salesOrder where order salesOrderId in (select * from salesOrder where status = ?) and lotDate = ? and floristId = ? and flowerId = ?



        // calculate for all order details group by florist
        //flowerId, qty,
                // 6 >> อายุดอกไม้ -1

        // วันที่สั่งซื้อ = lotDate = คำนวณจากวันปัจจุบัน + อายุดอกไม้(จากข้อ 6)
        //        วันที่สั่งซื้อ = วันที่ 5


//        lotDay >> select * from lot where day = calWeekOfDay(วันที่สั่งซื้อ)
//        หาวันที่ว่า lotDay ไปตกเป็นวันไหนได้บ้าง monday > 1,8,
//        lotDate(4) = มากสุดที่น้อยกว่าวันที่ (วันที่สั่งซื้อ 5)
//        select * from PO where date = ${lotDate}



//        ไม่เจอใบสั่งซื้อ
//                select * from pack where flowerId=${flowerId}
//        เลือก qty ที่ใกล้เคียงที่สุด >> pack(1,5,10)  ต้องใช้(3) = pack(1) * 3
//        เลือก qty ที่ใกล้เคียงที่สุด >> pack(1,5,10)  ต้องใช้(8) = pack(5) * 1 , pack(1) * 3

//        lotDate < วันที่สั่งซื้อ
//        for(var salesOrderDetail : confirmedSalesOrderDetails){
//            var nearlyPreparationDate = getNearlyPreparationDate(confirmedSalesOrderDetails);
//            processSalesOrderDetail(salesOrderDetail);
//        }

    }

    public void addCalculateSaleOrderDetailDto(List<CalculateSaleOrderDetailDto> calculateSaleOrderDetailsDto, CalculateSaleOrderDetailDto newItem){
        var selectedItem = calculateSaleOrderDetailsDto.stream()
                .filter(item -> item.getLotDate().equals(newItem.getLotDate())
                        && item.getFloristId().equals(newItem.getFloristId())
                        && item.getFlowerId().equals(newItem.getFlowerId())
                )
                .findFirst().orElse(null);
        if(selectedItem == null){
            calculateSaleOrderDetailsDto.add(newItem);
        }else{
            selectedItem.setQuantity(selectedItem.getQuantity() + newItem.getQuantity());
        }
    }

    public LocalDate getSaleOrderDeliveryDate(Date date){
        var localDate = DateUtil.toLocalDate(date);
        return localDate.minusDays(1);

    }

    public LocalDate getLotDate(LocalDate deliveryDate, Integer lotNo, Integer liftTime){
//        loop by flower-id
//          flower.lot-no > มาหาใน lot.lot-no > แล้วดูว่ามี day อะไรบ้าง
//        select * from lot where lotNo = flower.lot-no
        LocalDate availableDate = deliveryDate.minusDays((liftTime - 1));
        List<Lot> lots = lotService.findByLotNoOrderByDayAsc(lotNo);
        if(CollectionUtils.isEmpty(lots)){
            return null;
        }
        int availableDateDayOfWeek = availableDate.getDayOfWeek().getValue();
        log.info("availableDate: {}", availableDate);
        log.info("availableDateDayOfWeek: {}", availableDateDayOfWeek);
        Integer minNextDiffDay = null;

        for(Lot lot: lots){
            Integer lotDayOfWeek = lot.getDay();
            log.info("process lotDayOfWeek: {}", lotDayOfWeek);
            Integer currentNextDiffDay = null;
            if(lotDayOfWeek == availableDateDayOfWeek){
                currentNextDiffDay = 0;
            }else if(lotDayOfWeek > availableDateDayOfWeek){
                currentNextDiffDay = lotDayOfWeek - availableDateDayOfWeek;
            }else if(lotDayOfWeek < availableDateDayOfWeek){
                int availableDateDayOfWeekCal = 7 - availableDateDayOfWeek;
                currentNextDiffDay = availableDateDayOfWeekCal + lotDayOfWeek;
            }
            log.info("currentNextDiffDay: {}", currentNextDiffDay);
            if(minNextDiffDay == null){
                minNextDiffDay = currentNextDiffDay;
            }else{
                if(currentNextDiffDay < minNextDiffDay){
                    minNextDiffDay = currentNextDiffDay;
                }
            }
            log.info("minNextDiffDay: {}", minNextDiffDay);
        }

        if(minNextDiffDay == null){
            return null;
        }
        return availableDate.plusDays(minNextDiffDay);
//        จะได้ day of week
//        วันที่สามารถสั่งได้ = วันที่สั่งซื้อ - อายุดอกไม้(หลัง - 1 แล้ว)
//        วันที่สามารถสั่งได้ = (10/04/2023) - ((7 ก่อนลบ)-1) = (04/04/2023)
//        ให้เลือกตั้งแต่วันที่ (04/04/2023) ไปได้เลย โดยเป็น lot-date >= (04/04/2023) และต้องใกล้กับวันที่สั่งซื้อมากสุด หรือเป็นวันเดียวกันกับวันที่สั่งซื้อเลยก็ได้
//        lot-date >= (04/04/2023) < ไม่เกินวันที่สั่งซื้อ
//        ต้องเอารายการ lot มาแปลงเป็นวันที่แล้วเทียบ
//                เลือก lot ล่าสุด >
//        วันที่สั่งซื้อ ย้อนหลังไป 7 วัน >> ควรจะได้มาสัก 1 วัน > เรียกว่า lot-date
//        1 > วันที่เท่าไหร่ ในสัปดาห์ปัจจุบัน
//        3
//        5

    }
    public void processSalesOrderDetail(SalesOrderDetail salesOrderDetail){
        var formulaDetails = flowerFormulaDetailService.findAllByFlowerFormulaId(salesOrderDetail.getFlowerFormula().getId());
        for(var formulaDetail: formulaDetails){
            var flower = formulaDetail.getFlower();
            if(flower.getIsStock()){
                processStockFlower(salesOrderDetail, formulaDetail);
            }else{
                processNotStockFlower(salesOrderDetail, formulaDetail);
            }
        }
    }
    public void processStockFlower(SalesOrderDetail salesOrderDetail, FlowerFormulaDetail formulaDetail){ // 7
        // find by flower lifetime
        var stocks = new ArrayList<Stock>();
        if(!CollectionUtils.isEmpty(stocks)){ // exists

            return;
        }
    }
    public void processNotStockFlower(SalesOrderDetail salesOrderDetail, FlowerFormulaDetail formulaDetail){ // 7
        var floristId = salesOrderDetail.getFlorist().getId();
        var flower = formulaDetail.getFlower();
        var saleOrder = salesOrderDetail.getSalesOrder();
        var deliveryDate = DateUtil.toLocalDate(saleOrder.getDeliveryDateTime());

        var lotDate = getLotDate(deliveryDate, flower.getLotNo(), flower.getLifeTime());
        var addFlowerQty = salesOrderDetail.getQuantity() * formulaDetail.getQuantity();
        var draftPurchaseOrders = getDraftPurchaseOrder(lotDate);
        if(CollectionUtils.isEmpty(draftPurchaseOrders)){ // draft PO is not exists
            draftPurchaseOrders = new ArrayList<PurchaseOrder>();
            draftPurchaseOrders.add(purchaseOrderService.create(PurchaseOrder.builder()
                    .date(DateUtil.getDate(lotDate))
                    .status(CommonConstant.PO_STATUS_DRAFT)
                    .total(0d)
                    .transportationFee(0d)
                    .build()));
        }
        var draftPurchaseOrder = draftPurchaseOrders.get(0);
        var draftPurchaseOrderDetails = purchaseOrderDetailService.findByPurchaseOrderIdAndFloristIdAndFlowerId(draftPurchaseOrder.getId(), floristId, flower.getId());
        if(CollectionUtils.isEmpty(draftPurchaseOrderDetails)){

        }



//        var existsPurchaseOrderDetailByFlowerIdAndFloristId = draftPurchaseOrderDetails.stream()
//                .filter(item ->
//                        item.getFlowerId().equals(flower.getId()) &&
//                                item.getFloristId().equals(salesOrderDetail.getFlorist().getId()))
//                .collect(Collectors.toList());


//        PurchaseOrderDetail purchaseOrderDetailByFlowerIdAndFloristId = null;
//        if(CollectionUtils.isEmpty(existsPurchaseOrderDetailByFlowerIdAndFloristId)){
//            purchaseOrderDetailByFlowerIdAndFloristId = PurchaseOrderDetail.builder()
//                    .purchaseOrder(draftPurchaseOrder)
//                    .flowerId(flower.getId())
//                    .floristId(salesOrderDetail.getFlorist().getId())
//                    .lot(DateUtil.getDate(lotDate))
//                    .quantity(0)
//                    .receivedQty(0)
////                    .vehicle()
//                    .build();
//        }
        calculatePurchaseOrderDetail(null, addFlowerQty);
//        var purchaseOrderSaved = purchaseOrderDetailService.save(purchaseOrderDetailByFlowerIdAndFloristId);
        // re-calculate
    }
//    public LocalDate getLotDate(Flower flower){ // 7B
//        return LocalDate.now();
//    }
    public List<PurchaseOrder> getDraftPurchaseOrder(LocalDate orderDate){ //8B
        return purchaseOrderService.findByStatusAndOrderDate(CommonConstant.PO_STATUS_DRAFT, DateUtil.getDate(orderDate));
    }
    public List<Pack> getPack(Integer flowerId){
        // get pack by flowerId order by unitPack
        return packService.findByFlowerIdOrderByUnitPackDsc(flowerId);
    }
    public void calculatePurchaseOrderDetail(PurchaseOrderDetail purchaseOrderDetail, Integer addQty){

        var totalQty = purchaseOrderDetail.getQuantity() + addQty;
        var packs = getPack(purchaseOrderDetail.getFlowerId());
        // calculate pack
        log.info("packs: {}", packs);
        if(CollectionUtils.isEmpty(packs)){
            throw new CommonException("Pack does not exists for flowerId["+purchaseOrderDetail.getFlowerId()+"]");
        }


        var remainingQty = totalQty;
        for(;remainingQty == 0;){
            for(Pack pack: packs){
                var unitPack = pack.getUnitPack();
                if(unitPack <= remainingQty){

                }
            }

            Pack minUnitPack = packs.get(packs.size()-1);
            if(minUnitPack.getUnitPack() > remainingQty){

            }
        }





//        purchaseOrderDetail.setSupplierId();
//        purchaseOrderDetail.setPackId();
//        purchaseOrderDetail.setQuantity();

    }
//    public boolean inStock(){
//
//    }


// 7A วันที่สั่งซื้อ มาเทียบกับ lot ใน stock >> อายุดอกไม้ใน stock คงเหลือ
// อายุดอกไม้ใน stock คงเหลือ >= อายุของดอกไม้
// >> แสดงว่าใช้ใน stock ได้ >> ดึงจำนวน qty ออกมา
// >> ถ้ามีพอ >> หักออก จากจำนวน qty จบเลย
// >> ถ้ามีไม่พอ >> stock(2) แต่ต้องใช้(5) >> ตัด stock ก่อน 2(เท่าที่มี)
//          ยังขาดเท่าไหร่ จะต้องซื้อเพิ่ม
}

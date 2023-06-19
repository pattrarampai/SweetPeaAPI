package com.sweetpeatime.sweetpeatime.business;

import com.sweetpeatime.sweetpeatime.common.constant.CommonConstant;
import com.sweetpeatime.sweetpeatime.common.exception.CommonException;
import com.sweetpeatime.sweetpeatime.common.utils.DateUtil;
import com.sweetpeatime.sweetpeatime.common.utils.TransportationUtil;
import com.sweetpeatime.sweetpeatime.dto.auto.CalculatePackDto;
import com.sweetpeatime.sweetpeatime.dto.auto.CalculateSaleOrderDetailDto;
import com.sweetpeatime.sweetpeatime.entities.*;
import com.sweetpeatime.sweetpeatime.services.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.LocalDate;
import java.util.*;
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
    private final FlowerPriceService flowerPriceService;
    private final SupplierService supplierService;
    private final StockService stockService;
    private final FlowerService flowerService;

    public void confirmOrder(){
        // select ORDER where status = 'CONFIRM';
        List<SalesOrder> confirmedSaleOrderItems = salesOrderService.findByStatusOrderByDeliveryDateTimeAsc(CommonConstant.SALE_ORDER_STATUS_CONFIRM);
        if(CollectionUtils.isEmpty(confirmedSaleOrderItems)){
            return;
        }
        SalesOrder firstSalesOrder = confirmedSaleOrderItems.get(0);
        LocalDate minOrderDate = getSaleOrderDeliveryDate(firstSalesOrder.getDeliveryDateTime());
        log.info("minOrderDate: {}", minOrderDate);// ต้องดึงของแต่ florist หรือไม่

        List<SalesOrderDetail> saleOrderDetails = salesOrderDetailService.findByStatus(CommonConstant.SALE_ORDER_STATUS_CONFIRM);
        // get saleOrder detail ออกมาทั้งหมด ตามที่ CONFIRM แล้ว >> floristId >> order by florist
        log.info("{}", saleOrderDetails.size());

        List<CalculateSaleOrderDetailDto> calculateSaleOrderDetailsDto = new ArrayList();

        log.info("calculate and prepare data process");
        for(SalesOrderDetail saleOrderDetail: saleOrderDetails){
            var saleOrder = saleOrderDetail.getSalesOrder();
            var orderDate = DateUtil.toLocalDate(saleOrder.getDeliveryDateTime()).minusDays(1);
            var formulaDetails = flowerFormulaDetailService.findAllByFlowerFormulaId(saleOrderDetail.getFlowerFormula().getId());
            for(var formulaDetail: formulaDetails){
                var floristId = saleOrderDetail.getFlorist().getId();
                var flower = formulaDetail.getFlower();
                var lotDate = getLotDate(flower.getIsStock()?minOrderDate:orderDate, flower.getLotNo());
                var qty = saleOrderDetail.getQuantity() * formulaDetail.getQuantity();
                addCalculateSaleOrderDetailDto(calculateSaleOrderDetailsDto,
                        CalculateSaleOrderDetailDto.builder()
                                .orderDate(flower.getIsStock()?minOrderDate:orderDate)
                                .lotDate(lotDate)
                                .floristId(floristId)
                                .flowerId(flower.getId())
                                .liftTime(flower.getLifeTime() - 1)
                                .isStock(flower.getIsStock())
                                .quantity(qty)
                                .saleOrderDetail(saleOrderDetail)
                                .flowerFormulaDetails(formulaDetails)
                        .build());
            }
        }

        log.info("purchase order process");

        for(var calculateSalesOrderDetail : calculateSaleOrderDetailsDto){
            if(calculateSalesOrderDetail.getIsStock()){
                processStockFlower(calculateSalesOrderDetail);
            }else{
                processNotStockFlower(calculateSalesOrderDetail);
            }
        }

    }

    public static void addCalculateSaleOrderDetailDto(List<CalculateSaleOrderDetailDto> calculateSaleOrderDetailsDto, CalculateSaleOrderDetailDto newItem){
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

    public LocalDate getLotDate(LocalDate orderDate, Integer lotNo){
        List<Lot> lots = lotService.findByLotNoOrderByDayAsc(lotNo);
        if(CollectionUtils.isEmpty(lots)){
            return null;
        }
        int orderDateDayOfWeek = orderDate.getDayOfWeek().getValue();
        log.info("orderDate: {}", orderDate);
        log.info("orderDateDayOfWeek: {}", orderDateDayOfWeek);
        Integer minNextDiffDay = null;

        for(Lot lot: lots){
            Integer lotDayOfWeek = lot.getDay();
            log.info("process lotDayOfWeek: {}", lotDayOfWeek);
            Integer currentNextDiffDay = null;
            if(lotDayOfWeek == orderDateDayOfWeek){
                currentNextDiffDay = 0;
            }else if(lotDayOfWeek < orderDateDayOfWeek){
                currentNextDiffDay = orderDateDayOfWeek - lotDayOfWeek;
            }else if(lotDayOfWeek > orderDateDayOfWeek){
                int lotDayOfWeekDayOfWeekCal = 7 - lotDayOfWeek;
                currentNextDiffDay = lotDayOfWeekDayOfWeekCal + orderDateDayOfWeek;
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
        return orderDate.minusDays(minNextDiffDay);
    }

    public void processStockFlower(CalculateSaleOrderDetailDto calculateSalesOrderDetail) {
        var qty = calculateSalesOrderDetail.getQuantity();
        var lotDate = calculateSalesOrderDetail.getLotDate();
        var floristId = calculateSalesOrderDetail.getFloristId();
        var flowerId = calculateSalesOrderDetail.getFlowerId();
        var orderDate = calculateSalesOrderDetail.getOrderDate();
        var liftTime = calculateSalesOrderDetail.getLiftTime();
        var expireDate = orderDate.minusDays(liftTime);
        var stocks = stockService.findAllByFloristIdAndFlowerId(calculateSalesOrderDetail.getFloristId(), calculateSalesOrderDetail.getFlowerId());
        var remaining = 0;
        if(!CollectionUtils.isEmpty(stocks)){
            for(Stock stockItem : stocks){
                var stockLotDate = DateUtil.toLocalDate(stockItem.getLot());
                if(stockLotDate.compareTo(expireDate) >= 0){
                    remaining += stockItem.getQuantity();
                }
            }
        }

        var poOrderQty = qty;
        if(remaining > 0){
            if(poOrderQty > remaining){
                poOrderQty = poOrderQty - remaining;
            }else{
                poOrderQty = 0;
            }
        }
        if(poOrderQty > 0) {
            // ต้องซื้อเพิ่ม
            calculateSalesOrderDetail.setQuantity(poOrderQty);

            // หาใบ PO ด้วย
//            PO-date <= calculateSalesOrderDetail.getOrderDate();
            var draftPurchaseOrders = getDraftPurchaseOrderForStock(orderDate);
            PurchaseOrder po = null;

            if (!CollectionUtils.isEmpty(draftPurchaseOrders)) { // draft PO is exists
//            // ถ้ามี PO
//                findPODetail ที่มี flowerId และ floristId เดียวกัน
//                    ถ้าเจอ > ลบรายการออกแล้วสร้างใหม่
//                    ถ้าไม่เจอ > สร้างใหม่
                po = draftPurchaseOrders.get(0);
                var poId = po.getId();
                var poDetails = po.getPurchaseOrderDetail();

                // remove old item
                if (!CollectionUtils.isEmpty(poDetails)) {
                    var existingPoDetails = poDetails.stream()
                            .filter(item -> item.getFloristId().equals(floristId) && item.getFlowerId().equals(flowerId)).collect(Collectors.toList());
                    // remove old pack items by lotDate, floristId and flowerId
                    if (!CollectionUtils.isEmpty(existingPoDetails)) {
                        List<Integer> existingPoDetailsId = existingPoDetails
                                .stream()
                                .map((obj) -> obj.getId())
                                .collect(Collectors.toList());
                        purchaseOrderDetailService.deleteAllById(existingPoDetailsId);
                        po = purchaseOrderService.findById(poId);
                        poDetails = po.getPurchaseOrderDetail();
                    }
                }

                var flowerOpt = flowerService.findById(flowerId);
                if(!flowerOpt.isPresent()){
                    throw new CommonException("invalid flower id");
                }
                var flower = flowerOpt.get();
                lotDate = getLotDate(DateUtil.toLocalDate(po.getDate()), flower.getLotNo());
                var calculatePacks = calculatePack(calculateSalesOrderDetail);
                if(CollectionUtils.isEmpty(calculatePacks)){
                    throw new CommonException("calculate pack is empty");
                }
                // add new pack items
                for(CalculatePackDto calculatePackDto: calculatePacks){
                    // get price by priceId
                    var pack = calculatePackDto.getPack();
                    var packQty = calculatePackDto.getQuantity();
                    var totalWeight = pack.getPackWeight() * packQty;
                    var vehicleType = TransportationUtil.getVehicleType(totalWeight);
                    var transportationFee = TransportationUtil.getTransportationFee(vehicleType);
                    var flowerPrice = flowerPriceService.findById(pack.getPriceId());
                    var totalPrice = Double.valueOf(flowerPrice.getPrice() * packQty);
                    var supplier = supplierService.findFirstByDefault();
                    poDetails.add(PurchaseOrderDetail.builder()
                            .purchaseOrderId(po.getId())
                            .flowerId(calculateSalesOrderDetail.getFlowerId())
                            .packId(pack.getId())
                            .quantity(packQty)
                            .supplierId(supplier.getId())
                            .priceId(pack.getPriceId())
                            .lot(DateUtil.getDate(lotDate))
                            .floristId(calculateSalesOrderDetail.getFloristId())
                            .receivedQty(0)
                            .price(totalPrice)
                            .weight(totalWeight)
                            .vehicle(TransportationUtil.getVehicleType(totalWeight))
                            .transportationFee(transportationFee)
                            .build());
                }

                // calculate total for PO
                double totalPrice = 0;
                double totalTransportationFee = 0;
                for(PurchaseOrderDetail purchaseOrderDetail: poDetails){
                    totalPrice += purchaseOrderDetail.getPrice();
                    totalTransportationFee += purchaseOrderDetail.getTransportationFee();
                }
                // update PO and PO Detail
                po.setTotal(totalPrice);
                po.setTransportationFee(totalTransportationFee);
                purchaseOrderService.save(po);
            } else {
//            // ถ้าไม่มี PO
                    var formulaDetails = calculateSalesOrderDetail.getFlowerFormulaDetails();
                    var maxLiftTime = 0;
                    log.info("---- calculate maxLiftTime start");
                    for (var formulaDetail : formulaDetails) {
                        log.info("  formulaDetail.getFlower().getLifeTime(): {}", formulaDetail.getFlower().getLifeTime());
                        if (formulaDetail.getFlower().getLifeTime() > maxLiftTime) {
                            maxLiftTime = formulaDetail.getFlower().getLifeTime();
                        }
                    }
                    log.info("  result maxLiftTime: {}", maxLiftTime);
                    log.info("---- calculate maxLiftTime end");

//            PO-date <= calculateSalesOrderDetail.getOrderDate().plusDays(maxLiftTime - 1);
                    var maxLiftTimeDate = calculateSalesOrderDetail.getOrderDate().plusDays(maxLiftTime - 1);
                    var maxLiftTimeDraftPurchaseOrders = getDraftPurchaseOrderForStock(maxLiftTimeDate);
                    if(!CollectionUtils.isEmpty(maxLiftTimeDraftPurchaseOrders)){
//              // ถ้ามี
                        var maxLiftTimePo = maxLiftTimeDraftPurchaseOrders.get(0);
//                เปลี่ยนจาก draft > cancel
                        // update
                        maxLiftTimePo.setStatus(CommonConstant.PO_STATUS_CANCEL);
                        purchaseOrderService.save(maxLiftTimePo);
                    }
//              // ถ้าไม่มี หรือ มี(แต่ถูกยกเลิกก่อนหน้าแล้ว)
                // สร้างใหม่ PO-date จะเป็น minOrderDate
                    po = purchaseOrderService.save(PurchaseOrder.builder()
                            .date(DateUtil.getDate(orderDate))
                            .status(CommonConstant.PO_STATUS_DRAFT)
                            .total(0d)
                            .transportationFee(0d)
                            .purchaseOrderDetail(new ArrayList<>())
                            .build());

                    var poDetails = po.getPurchaseOrderDetail();
                    var calculatePacks = calculatePack(calculateSalesOrderDetail);
                    if(CollectionUtils.isEmpty(calculatePacks)){
                        throw new CommonException("calculate pack is empty");
                    }
                    // add new pack items
                    for(CalculatePackDto calculatePackDto: calculatePacks){
                        // get price by priceId
                        var pack = calculatePackDto.getPack();
                        var packQty = calculatePackDto.getQuantity();
                        var totalWeight = pack.getPackWeight() * packQty;
                        var vehicleType = TransportationUtil.getVehicleType(totalWeight);
                        var transportationFee = TransportationUtil.getTransportationFee(vehicleType);
                        var flowerPrice = flowerPriceService.findById(pack.getPriceId());
                        var totalPrice = Double.valueOf(flowerPrice.getPrice() * packQty);
                        var supplier = supplierService.findFirstByDefault();
                        poDetails.add(PurchaseOrderDetail.builder()
                                .purchaseOrderId(po.getId())
                                .flowerId(calculateSalesOrderDetail.getFlowerId())
                                .packId(pack.getId())
                                .quantity(packQty)
                                .supplierId(supplier.getId())
                                .priceId(pack.getPriceId())
                                .lot(DateUtil.getDate(lotDate))
                                .floristId(calculateSalesOrderDetail.getFloristId())
                                .receivedQty(0)
                                .price(totalPrice)
                                .weight(totalWeight)
                                .vehicle(TransportationUtil.getVehicleType(totalWeight))
                                .transportationFee(transportationFee)
                                .build());
                    }

                    // calculate total for PO
                    double totalPrice = 0;
                    double totalTransportationFee = 0;
                    for(PurchaseOrderDetail purchaseOrderDetail: poDetails){
                        totalPrice += purchaseOrderDetail.getPrice();
                        totalTransportationFee += purchaseOrderDetail.getTransportationFee();
                    }
                    // update PO and PO Detail
                    po.setTotal(totalPrice);
                    po.setTransportationFee(totalTransportationFee);
                    purchaseOrderService.save(po);
            }
        }
    }
    @Transactional
    public void processNotStockFlower(CalculateSaleOrderDetailDto calculateSaleOrderDetailDto){ // 7
        var calculatePacks = calculatePack(calculateSaleOrderDetailDto);
        if(CollectionUtils.isEmpty(calculatePacks)){
            throw new CommonException("calculate pack is empty");
        }
        var orderDate = calculateSaleOrderDetailDto.getOrderDate();
        var lotDate = calculateSaleOrderDetailDto.getLotDate();
        var floristId = calculateSaleOrderDetailDto.getFloristId();
        var flowerId = calculateSaleOrderDetailDto.getFlowerId();
        var draftPurchaseOrders = getDraftPurchaseOrder(orderDate);
        PurchaseOrder po = null;
        if(CollectionUtils.isEmpty(draftPurchaseOrders)){ // draft PO is not exists
            po = purchaseOrderService.save(PurchaseOrder.builder()
                    .date(DateUtil.getDate(orderDate))
                    .status(CommonConstant.PO_STATUS_DRAFT)
                    .total(0d)
                    .transportationFee(0d)
                    .purchaseOrderDetail(new ArrayList<>())
                    .build());
        }else{
            po = draftPurchaseOrders.get(0);
        }
        var poId = po.getId();
        var poDetails = po.getPurchaseOrderDetail();
        if(!CollectionUtils.isEmpty(poDetails)){
            var existingPoDetails = poDetails.stream()
                    .filter(item-> DateUtil.toLocalDate(item.getLot()).compareTo(lotDate) == 0 && item.getFloristId().equals(floristId) && item.getFlowerId().equals(flowerId)).collect(Collectors.toList());
            // remove old pack items by lotDate, floristId and flowerId
            if(!CollectionUtils.isEmpty(existingPoDetails)){
                List<Integer> existingPoDetailsId = existingPoDetails
                        .stream()
                        .map((obj) -> obj.getId())
                        .collect(Collectors.toList());
                purchaseOrderDetailService.deleteAllById(existingPoDetailsId);
                po = purchaseOrderService.findById(poId);
                poDetails = po.getPurchaseOrderDetail();
            }
        }

        // add new pack items
        for(CalculatePackDto calculatePackDto: calculatePacks){
            // get price by priceId
            var pack = calculatePackDto.getPack();
            var packQty = calculatePackDto.getQuantity();
            var totalWeight = pack.getPackWeight() * packQty;
            var vehicleType = TransportationUtil.getVehicleType(totalWeight);
            var transportationFee = TransportationUtil.getTransportationFee(vehicleType);
            var flowerPrice = flowerPriceService.findById(pack.getPriceId());
            var totalPrice = Double.valueOf(flowerPrice.getPrice() * packQty);
            var supplier = supplierService.findFirstByDefault();
            poDetails.add(PurchaseOrderDetail.builder()
                    .purchaseOrderId(po.getId())
                    .flowerId(calculateSaleOrderDetailDto.getFlowerId())
                    .packId(pack.getId())
                    .quantity(packQty)
                    .supplierId(supplier.getId())
                    .priceId(pack.getPriceId())
                    .lot(DateUtil.getDate(lotDate))
                    .floristId(calculateSaleOrderDetailDto.getFloristId())
                    .receivedQty(0)
                    .price(totalPrice)
                    .weight(totalWeight)
                    .vehicle(TransportationUtil.getVehicleType(totalWeight))
                    .transportationFee(transportationFee)
                    .build());
        }

        // calculate total for PO
        double totalPrice = 0;
        double totalTransportationFee = 0;
        for(PurchaseOrderDetail purchaseOrderDetail: poDetails){
            totalPrice += purchaseOrderDetail.getPrice();
            totalTransportationFee += purchaseOrderDetail.getTransportationFee();
        }
        // update PO and PO Detail
        po.setTotal(totalPrice);
        po.setTransportationFee(totalTransportationFee);
        purchaseOrderService.save(po);
    }
    public List<PurchaseOrder> getDraftPurchaseOrder(LocalDate orderDate){ //8B
        return purchaseOrderService.findByStatusAndOrderDate(CommonConstant.PO_STATUS_DRAFT, DateUtil.getDate(orderDate));
    }
    public List<PurchaseOrder> getDraftPurchaseOrderForStock(LocalDate orderDate){ //8B
        return purchaseOrderService.findByStatusAndDateForStock(CommonConstant.PO_STATUS_DRAFT, DateUtil.getDate(orderDate));
    }
    public List<Pack> getPack(Integer flowerId){
        // get pack by flowerId order by unitPack
        log.info("flowerId: {}", flowerId);
        return packService.findByFlowerIdOrderByUnitPackDesc(flowerId);
    }
    public List<CalculatePackDto> calculatePack(CalculateSaleOrderDetailDto calculateSaleOrderDetailDto){
        List<CalculatePackDto> result = new ArrayList<>();
        var totalQty = calculateSaleOrderDetailDto.getQuantity();
        var packs = getPack(calculateSaleOrderDetailDto.getFlowerId());
        // calculate pack
        log.info("packs: {}", packs);
        if(CollectionUtils.isEmpty(packs)){
            throw new CommonException("Pack does not exists for flowerId["+calculateSaleOrderDetailDto.getFlowerId()+"]");
        }
        var remainingQty = totalQty;
        for(;remainingQty > 0;){
            Pack selectedPack = null;
            for(Pack pack: packs){
                if(remainingQty > packs.get(0).getUnitPack()){
                    selectedPack = packs.get(0);
                }else{
                    var unitPack = pack.getUnitPack();
                    if(unitPack >= remainingQty){
                        selectedPack = pack;
                    }
                }
            }
            addPack(result, selectedPack);
            remainingQty -= selectedPack.getUnitPack();
        }
        return result;
    }

    public List<CalculatePackDto> calculatePack_old(CalculateSaleOrderDetailDto calculateSaleOrderDetailDto){
        List<CalculatePackDto> result = new ArrayList<>();
        var totalQty = calculateSaleOrderDetailDto.getQuantity();
        var packs = getPack(calculateSaleOrderDetailDto.getFlowerId());
        // calculate pack
        log.info("packs: {}", packs);
        if(CollectionUtils.isEmpty(packs)){
            throw new CommonException("Pack does not exists for flowerId["+calculateSaleOrderDetailDto.getFlowerId()+"]");
        }
        var remainingQty = totalQty;
        if(packs.get(0).getUnitPack() >= remainingQty){
            Pack selectedPack = null;
            for(Pack pack: packs){
                var unitPack = pack.getUnitPack();
                if(unitPack >= remainingQty){
                    selectedPack = pack;
                }
            }
            addPack(result, selectedPack);
        }else{
            for(;remainingQty > 0;){
                for(Pack pack: packs){
                    var unitPack = pack.getUnitPack();
                    for(;unitPack <= remainingQty;){
                        addPack(result, pack);
                        remainingQty = remainingQty - unitPack;
                    }
                }
                if(remainingQty > 0){
                    Pack minUnitPack = packs.get(packs.size()-1);
                    if(minUnitPack.getUnitPack() > remainingQty){
                        addPack(result, minUnitPack);
                        remainingQty = remainingQty - minUnitPack.getUnitPack();
                    }
                }
            }
        }
        return result;
    }

    public static void addPack(List<CalculatePackDto> totalPack, Pack newPack){
        var filterItem = totalPack.stream().filter(item-> item.getPack().getId().equals(newPack.getId())).findFirst().orElse(null);
        if(filterItem == null){
            totalPack.add(CalculatePackDto.builder()
                            .pack(newPack)
                            .quantity(1)
                            .build());
        }else{
            filterItem.setQuantity(filterItem.getQuantity() + 1);
        }
    }

}

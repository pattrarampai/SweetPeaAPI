package com.sweetpeatime.sweetpeatime.controllers;

import com.sweetpeatime.sweetpeatime.entities.*;
import com.sweetpeatime.sweetpeatime.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.util.Date;
import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping(value = "/purchase-order")
public class PurchaseOrderController {

    @Autowired
    private PurchaseOrderRepository purchaseOrderRepository;

    @Autowired
    private PurchaseOrderDetailRepository purchaseOrderDetailRepository;

    @PostMapping(value = "/create")
    public void create(@RequestBody PurchaseOrder req) {
        req.setStatus("DRAFT");
        for (int i = 0; i < req.getPurchaseOrderDetail().size(); i++) {
            req.getPurchaseOrderDetail().get(i).setReceivedQty(0);
        }
        purchaseOrderRepository.save(req);
    }

    @GetMapping(value = "/search")
    public List<PurchaseOrder> search(@RequestParam("startDate") String startDate,
            @RequestParam("endDate") String endDate) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        return purchaseOrderRepository.findByDateBetween(formatter.parse(startDate), formatter.parse(endDate));
    }

    @PostMapping(value = "/{id}/edit")
    public void edit(@PathVariable("id") Integer id, @RequestBody PurchaseOrder req) throws Exception {
        PurchaseOrder purchaseOrder = purchaseOrderRepository.findById(id)
                .orElseThrow(() -> new Exception("Your Purchase Order can't be found"));
        if (!purchaseOrder.getStatus().equals("DRAFT")) {
            throw new Exception("Your Purchase Order is Invalid");
        }
        req.setId(purchaseOrder.getId());
        req.setStatus(purchaseOrder.getStatus());
        for (int i = 0; i < req.getPurchaseOrderDetail().size(); i++) {
            req.getPurchaseOrderDetail().get(i).setReceivedQty(0);
        }
        List<PurchaseOrderDetail> purchaseOrderDetail = purchaseOrderDetailRepository.findByPurchaseOrderId(id);
        purchaseOrderDetailRepository.deleteAll(purchaseOrderDetail);
        purchaseOrderRepository.save(req);
    }

    @GetMapping(value = "/remind")
    public List<PurchaseOrder> remind() throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        return purchaseOrderRepository.findByStatusAndDate("DRAFT", formatter.parse(formatter.format(new Date())));
    }

    @PostMapping(value = "/{id}/confirm")
    public void confirm(@PathVariable("id") Integer id) throws Exception {
        PurchaseOrder purchaseOrder = purchaseOrderRepository.findById(id)
                .orElseThrow(() -> new Exception("Your Purchase Order can't be found"));
        if (!purchaseOrder.getStatus().equals("DRAFT")) {
            throw new Exception("Your Purchase Order is Invalid");
        }
        purchaseOrder.setStatus("CONFIRM");
        purchaseOrderRepository.save(purchaseOrder);
    }

    @GetMapping(value = "/accept")
    public List<PurchaseOrder> getAccept(@RequestParam("startDate") String startDate,
            @RequestParam("endDate") String endDate) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        return purchaseOrderRepository.findByDateBetweenAndStatus(formatter.parse(startDate), formatter.parse(endDate),
                "CONFIRM");
    }

    @PostMapping(value = "/{id}/accept/{status}")
    public void postAccept(@PathVariable("id") Integer id, @PathVariable("status") String status,
            @RequestBody PurchaseOrder req) throws Exception {
        if (!(status.equals("COMPLETED") || status.equals("PARTIAL_COMPLETED"))) {
            throw new Exception("Your Status is Invalid");
        }
        PurchaseOrder purchaseOrder = purchaseOrderRepository.findById(id).orElseThrow(() -> new Exception("Your Purchase Order can't be found"));
        if (!(purchaseOrder.getStatus().equals("CONFIRM") || purchaseOrder.getStatus().equals("PARTIAL_COMPLETED"))) {
            throw new Exception("Your Purchase Order is Invalid");
        }
        // PurchaseOrder purchaseOrder =
        // purchaseOrderRepository.findById(id).orElseThrow(() -> new
        // DataNotFoundException("cannot find profile id : " + profileId));;
        purchaseOrderRepository.save(req);
    }

}

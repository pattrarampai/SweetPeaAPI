package com.sweetpeatime.sweetpeatime.controllers;

import com.sweetpeatime.sweetpeatime.entities.*;
import com.sweetpeatime.sweetpeatime.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping(value = "/purchase-order")
public class PurchaseOrderController {

    @Autowired
    private PurchaseOrderRepository purchaseOrderRepository;

    @PostMapping(value = "/create")
    public void create(@RequestBody PurchaseOrder req) throws ParseException {
        req.setStatus("DRAFT");
        purchaseOrderRepository.save(req);
    }

    @GetMapping(value = "/search")
    public List<PurchaseOrder> search(@RequestParam("startDate") String startDate, @RequestParam("endDate") String endDate) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        return purchaseOrderRepository.findByDateBetween(formatter.parse(startDate),formatter.parse(endDate));
    }

    @PostMapping(value = "/{id}/edit")
    public void edit(@PathVariable("id") Integer id) throws ParseException {
        PurchaseOrder purchaseOrder = purchaseOrderRepository.findByIdAndStatus(id, "DRAFT");
        purchaseOrder.setStatus("CONFIRM");
        purchaseOrderRepository.save(purchaseOrder);
    }

    @GetMapping(value = "/remind")
    public List<PurchaseOrder> remind() throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        return purchaseOrderRepository.findByStatusAndDate("DRAFT", formatter.parse(formatter.format(new Date())));
    }

    @PostMapping(value = "/{id}/confirm")
    public void confirm(@PathVariable("id") Integer id) throws ParseException {
        PurchaseOrder purchaseOrder = purchaseOrderRepository.findByIdAndStatus(id, "DRAFT");
        purchaseOrder.setStatus("CONFIRM");
        purchaseOrderRepository.save(purchaseOrder);
    }

    @GetMapping(value = "/accept")
    public List<PurchaseOrder> getAccept(@RequestParam("startDate") String startDate, @RequestParam("endDate") String endDate) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        return purchaseOrderRepository.findByDateBetweenAndStatus(formatter.parse(startDate),formatter.parse(endDate), "CONFIRM");
    }

    @PostMapping(value = "/{id}/accept")
    public void postAccept(@RequestBody PurchaseOrder req) throws ParseException {
        req.setStatus("DRAFT");
        purchaseOrderRepository.save(req);
    }

}

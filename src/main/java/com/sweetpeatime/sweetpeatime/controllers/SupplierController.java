package com.sweetpeatime.sweetpeatime.controllers;


import com.sweetpeatime.sweetpeatime.entities.Supplier;
import com.sweetpeatime.sweetpeatime.repositories.SupplierRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.text.ParseException;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping(value = "/supplier")
public class SupplierController {

    @Autowired
    SupplierRepository supplierRepository;

    @PersistenceContext
    EntityManager entityManager;

    @GetMapping(value="/getAll")
    public List<Supplier> getAllSupplier() {
        return this.supplierRepository.findAll();
    }

    @GetMapping(value = "/supplier/{supplierId}")
    public List<Supplier> getSupplierById(@PathVariable("supplierId") Integer supplierId) throws ParseException {
        return (List<Supplier>) supplierRepository.findSupplierById(supplierId);
    }
}



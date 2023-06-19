package com.sweetpeatime.sweetpeatime.services;

import com.sweetpeatime.sweetpeatime.common.exception.CommonException;
import com.sweetpeatime.sweetpeatime.entities.Supplier;
import com.sweetpeatime.sweetpeatime.repositories.SupplierRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class SupplierService {
    @Autowired
    private SupplierRepository supplierRepository;
    public Supplier findFirstByDefault(){
        return supplierRepository.findByDefaultActive(true).orElseThrow(() -> new CommonException("Your Default Supplier can't be found"));
    }


}

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
@RequestMapping(value = "/flower-price")
public class FlowerPriceController {

    @Autowired
    private FlowerPriceRepository flowerPriceRepository;

    @GetMapping(value = "/flower/{flowerId}")
    public List<FlowerPrice> getFlowerPriceByFlowerId(@PathVariable("flowerId") Integer flowerId) throws ParseException {
        return flowerPriceRepository.findAllByFlowerId(flowerId);
    }
    
}

package com.sweetpeatime.sweetpeatime.controllers;

import com.sweetpeatime.sweetpeatime.entities.Flower;
import com.sweetpeatime.sweetpeatime.repositories.FlowerRepository;
import com.sweetpeatime.sweetpeatime.repositories.PackRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.sweetpeatime.sweetpeatime.entities.Pack;
import java.text.ParseException;
import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping(value="/flower")
public class FlowerController {

    @Autowired
    private FlowerRepository flowerRepository;
    @Autowired
    PackRepository packRepository;

    public FlowerController(PackRepository packRepository) {
        this.packRepository = packRepository;
    }

    @GetMapping(value="/getAll")
    public List<Flower> getAllFlowers() {
        return this.flowerRepository.findAll();
    }

    @GetMapping(value="/getLifeTimeFlower")
    public List<Flower> getLifeTimeFlower(@RequestParam("flowerId") Integer flowerId,@RequestParam("flowerType") String flowerType) throws ParseException {
        return this.flowerRepository.findAllByIdAndFlowerType(flowerId, flowerType);
    }

    @GetMapping(value="/getPackByFlowerId/{flowerId}")
    public List<Pack> getPackByFlowerId(@PathVariable ("flowerId") Integer flowerId) throws ParseException
    {return  this.packRepository.getPacksByFlowerId(flowerId);}

    @GetMapping(value = "getAllPack")
    public List<Pack> getAllPack(){return packRepository.findAll();}
}
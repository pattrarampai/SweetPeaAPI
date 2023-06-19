package com.sweetpeatime.sweetpeatime.services;

import com.sweetpeatime.sweetpeatime.common.exception.CommonException;
import com.sweetpeatime.sweetpeatime.entities.FlowerPrice;
import com.sweetpeatime.sweetpeatime.repositories.FlowerPriceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class FlowerPriceService {
    private final FlowerPriceRepository flowerPriceRepository;
    public FlowerPrice findById(Integer flowerId){
        return flowerPriceRepository.findById(flowerId).orElseThrow(() -> new CommonException("Your Flower Price ID [flowerId] can't be found"));
    }
}

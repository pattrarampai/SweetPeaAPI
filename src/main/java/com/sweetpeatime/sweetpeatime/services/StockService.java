package com.sweetpeatime.sweetpeatime.services;

import com.sweetpeatime.sweetpeatime.entities.Stock;
import com.sweetpeatime.sweetpeatime.repositories.StockRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class StockService {
    private final StockRepository stockRepository;
    public List<Stock> findAllByFloristIdAndFlowerId(Integer floristId, Integer flowerId){
        return stockRepository.findAllByFloristIdAndFlowerId(floristId, flowerId);
    }
}

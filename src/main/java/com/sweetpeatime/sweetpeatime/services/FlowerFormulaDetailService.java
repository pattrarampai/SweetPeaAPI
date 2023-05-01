package com.sweetpeatime.sweetpeatime.services;

import com.sweetpeatime.sweetpeatime.entities.FlowerFormulaDetail;
import com.sweetpeatime.sweetpeatime.repositories.FlowerFormulaDetailRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class FlowerFormulaDetailService {
    private final FlowerFormulaDetailRepository flowerFormulaDetailRepository;
    public List<FlowerFormulaDetail> findAllByFlowerFormulaId(Integer id){
        return flowerFormulaDetailRepository.findAllByFlowerFormulaId(id);
    }

}

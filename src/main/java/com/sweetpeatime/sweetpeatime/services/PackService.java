package com.sweetpeatime.sweetpeatime.services;

import com.sweetpeatime.sweetpeatime.entities.Pack;
import com.sweetpeatime.sweetpeatime.repositories.PackRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class PackService {
    private final PackRepository packRepository;
    public List<Pack> findByFlowerIdOrderByUnitPackDesc(Integer flowerId){
        return packRepository.findByFlowerIdOrderByUnitPackDesc(flowerId);
    }
}

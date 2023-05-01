package com.sweetpeatime.sweetpeatime.services;

import com.sweetpeatime.sweetpeatime.entities.Lot;
import com.sweetpeatime.sweetpeatime.entities.Pack;
import com.sweetpeatime.sweetpeatime.repositories.LotRepository;
import com.sweetpeatime.sweetpeatime.repositories.PackRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class LotService {
    private final LotRepository lotRepository;
    public List<Lot> findByLotNoOrderByDayAsc(Integer lotNo){
        return lotRepository.findByLotNoOrderByDayAsc(lotNo);
    }
}

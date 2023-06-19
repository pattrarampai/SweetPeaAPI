package com.sweetpeatime.sweetpeatime.repositories;

import com.sweetpeatime.sweetpeatime.entities.Stock;
import com.sweetpeatime.sweetpeatime.entities.StockReceived;

import java.util.Date;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StockReceivedRepository extends JpaRepository<StockReceived, Integer> {

    StockReceived findByFlowerIdAndLot(Integer flowerId, Date lot);
    
}

package com.sweetpeatime.sweetpeatime.repositories;

import com.sweetpeatime.sweetpeatime.entities.FlowerPrice;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface FlowerPriceRepository extends JpaRepository<FlowerPrice, Integer> {
    FlowerPrice findByFlowerId(Integer flowerId);

    List<FlowerPrice> findAllByFlowerId(Integer flowerId);

    List<FlowerPrice> findByFlower_IdAndPackId(Integer flowerId, Integer packId);

//    List<FlowerPrice> findAllByFlowerIdAndPackId(Integer flowerId, Integer packId);
}

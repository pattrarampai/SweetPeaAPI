package com.sweetpeatime.sweetpeatime.repositories;
import com.sweetpeatime.sweetpeatime.entities.Pack;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PackRepository extends JpaRepository<Pack, Integer> {

    List<Pack> getPacksByFlowerId(Integer flowerId);

    @Query("select p from Pack p")
    @Override
    List<Pack> findAll();
}

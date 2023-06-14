package com.sweetpeatime.sweetpeatime.repositories;
import com.sweetpeatime.sweetpeatime.entities.Pack;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PackRepository extends JpaRepository<Pack, Integer> {
    List<Pack> getPacksByFlowerId(Integer flowerId);

}

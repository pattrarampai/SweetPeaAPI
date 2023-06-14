package com.sweetpeatime.sweetpeatime.repositories;
import com.sweetpeatime.sweetpeatime.entities.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SupplierRepository extends JpaRepository<Supplier, Integer> {

    Supplier findSupplierById(Integer id);

    List<Supplier>findAll();

    Supplier findSupplierByName(String name);
}

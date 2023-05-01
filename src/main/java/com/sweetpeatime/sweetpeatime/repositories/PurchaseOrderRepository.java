package com.sweetpeatime.sweetpeatime.repositories;

import com.sweetpeatime.sweetpeatime.entities.PurchaseOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface PurchaseOrderRepository extends JpaRepository<PurchaseOrder, Integer> {

    List<PurchaseOrder> findByDateBetween(Date startDate, Date endDate);

    PurchaseOrder findByIdAndStatus(Integer id, String status);

    List<PurchaseOrder> findByStatusAndDate(String status, Date date);

    List<PurchaseOrder> findByDateBetweenAndStatus(Date startDate, Date endDate, String status);

    List<PurchaseOrder> findByStatus(String status);
}

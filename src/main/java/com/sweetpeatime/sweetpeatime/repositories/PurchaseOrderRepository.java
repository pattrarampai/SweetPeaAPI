package com.sweetpeatime.sweetpeatime.repositories;

import com.sweetpeatime.sweetpeatime.entities.PurchaseOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface PurchaseOrderRepository extends JpaRepository<PurchaseOrder, Integer> {

    List<PurchaseOrder> findByDateBetween(Date startDate, Date endDate);

    PurchaseOrder findByIdAndStatus(Integer id, String status);

//    @Query(value =
//            "SELECT * FROM PurchaseOrder po " +
//                    "WHERE po.status = :status and po.date = :date "
//                    , nativeQuery = true)
    List<PurchaseOrder> findByStatusAndDate(String status, Date date);

        @Query(value =
            "SELECT * FROM PurchaseOrder po " +
                    "WHERE po.status = :status and po.date <= :date order by date desc"
                    , nativeQuery = true)
    List<PurchaseOrder> findByStatusAndDateForStock(String status, Date date);

    List<PurchaseOrder> findByDateBetweenAndStatus(Date startDate, Date endDate, String status);

    List<PurchaseOrder> findByStatus(String status);
}

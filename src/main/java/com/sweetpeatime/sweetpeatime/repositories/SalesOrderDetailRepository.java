package com.sweetpeatime.sweetpeatime.repositories;

import com.sweetpeatime.sweetpeatime.entities.SalesOrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SalesOrderDetailRepository extends JpaRepository<SalesOrderDetail, Integer> {
    List<SalesOrderDetail> findAllBySalesOrderId(Integer salesOderId);

    @Query(value =
            "SELECT * FROM SalesOrderDetail sd " +
            "WHERE sd.salesOrderId in (select id from SalesOrder where status = :status) " +
            "ORDER BY floristId" , nativeQuery = true)
    List<SalesOrderDetail> findByStatus(String status);
}

package com.sweetpeatime.sweetpeatime.entities.dto.auto;

import com.sweetpeatime.sweetpeatime.entities.Flower;
import com.sweetpeatime.sweetpeatime.entities.FlowerFormulaDetail;
import com.sweetpeatime.sweetpeatime.entities.SalesOrderDetail;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class CalculateSaleOrderDetailDto {
    private SalesOrderDetail saleOrderDetail;
    private List<FlowerFormulaDetail> flowerFormulaDetails;
    private LocalDate orderDate;
    private LocalDate lotDate;
    private Integer floristId;
    private Integer flowerId;
    private Integer liftTime;
    private Boolean isStock;
    private Integer quantity;
}

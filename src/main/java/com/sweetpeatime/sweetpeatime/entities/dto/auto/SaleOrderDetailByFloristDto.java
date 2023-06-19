package com.sweetpeatime.sweetpeatime.entities.dto.auto;

import com.sweetpeatime.sweetpeatime.entities.Flower;
import com.sweetpeatime.sweetpeatime.entities.SalesOrderDetail;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class SaleOrderDetailByFloristDto {
    private Long floristId;
    private List<SalesOrderDetail> orderDetails;
}

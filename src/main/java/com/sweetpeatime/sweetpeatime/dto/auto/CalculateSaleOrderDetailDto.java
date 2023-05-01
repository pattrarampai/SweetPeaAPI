package com.sweetpeatime.sweetpeatime.dto.auto;

import com.sweetpeatime.sweetpeatime.entities.Flower;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class CalculateSaleOrderDetailDto {
    private LocalDate deliveryDate;
    private LocalDate lotDate;
    private Integer floristId;
    private Integer flowerId;
    private Integer liftTime;
    private Boolean isStock;
    private Integer quantity;
}

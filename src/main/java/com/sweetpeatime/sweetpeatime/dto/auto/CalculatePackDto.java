package com.sweetpeatime.sweetpeatime.dto.auto;

import com.sweetpeatime.sweetpeatime.entities.Pack;
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
public class CalculatePackDto {
    private Pack pack;
    private Integer quantity;
}

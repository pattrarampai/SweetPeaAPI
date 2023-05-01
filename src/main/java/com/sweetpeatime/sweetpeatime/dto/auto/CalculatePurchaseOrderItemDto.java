package com.sweetpeatime.sweetpeatime.dto.auto;

import com.sweetpeatime.sweetpeatime.entities.Flower;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class CalculatePurchaseOrderItemDto {
    private Flower flower;
    private Integer totalQty;
    public void addQty(Integer addQty){
        this.totalQty = this.totalQty + addQty;
    }
}

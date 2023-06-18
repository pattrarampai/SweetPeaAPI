package com.sweetpeatime.sweetpeatime.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PurchaseOrderAcceptReq {

    private Integer purchaseOrderDetailId;
    private Integer receivedQty;
    
}

package com.duykypaul.wmanage_api.payload.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderReq {
    private String customer;
    private String deliveryAddress;
    private Date deliveryDate;
    private String branchCode;
}

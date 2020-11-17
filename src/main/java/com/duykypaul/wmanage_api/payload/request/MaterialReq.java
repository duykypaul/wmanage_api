package com.duykypaul.wmanage_api.payload.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MaterialReq {
    private String branchCode;
    private String dimension;
    private String materialType;
    private Integer length;
    private Integer quantity;
}

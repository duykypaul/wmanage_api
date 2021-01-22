package com.duykypaul.wmanage_api.beans;

import lombok.*;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class ToriaiRetsuBean extends BaseBean<ToriaiRetsuBean> {
    private String toriaiHeadNo;
    private Integer retsuNo;
    private Integer length;
    private Integer quantity;

    private Integer lengthUsed;
    private Integer lengthRemaining;
    private String bozaimotoToriaiHeadNo;
    private String listMaterialNo;
}

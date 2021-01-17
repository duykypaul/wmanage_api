package com.duykypaul.wmanage_api.algorithm.fast;

import lombok.*;

@EqualsAndHashCode()
@ToString()
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FastCutBean {
    private Integer numberMaterial;
    private Integer remain;
    private int[] arrIndexStockUsed;
}

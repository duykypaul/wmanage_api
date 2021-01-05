package com.duykypaul.wmanage_api.beans;

import lombok.*;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class MaterialBean extends BaseBean<MaterialBean> {
    private String materialNo;
    private Integer length;
    private String status;// active, inactive, plan
    private String cutterHeadNo;
    private String cutterHeadNoUsed;
    private String cutterLineNo;
    private String cutterMaterialNo;
    private String cutterMaterialNoIndex;
    private String cutterUsedMaterialNo;
    private String cutterUsedMaterialNoIndex;
    private BranchBean branch;
    private MaterialTypeBean materialType;
}

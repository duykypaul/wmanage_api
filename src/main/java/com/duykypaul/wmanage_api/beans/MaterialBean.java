package com.duykypaul.wmanage_api.beans;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
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

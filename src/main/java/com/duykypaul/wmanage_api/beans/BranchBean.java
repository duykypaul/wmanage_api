package com.duykypaul.wmanage_api.beans;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BranchBean extends BaseBean<BranchBean> {
    private String branchCode;
    private String branchName;
    private Integer wattage;
}

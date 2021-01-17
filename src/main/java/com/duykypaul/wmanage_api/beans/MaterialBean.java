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
    private String seiKbn;
    private Integer length;
    private String status;// active, inactive, plan
    private String toriaiHeadNo; // sinh ra
    private String toriaiHeadNoUsed; // duoc su dung boi
    private String toriaiGyoNo; // dùng cho hàng nào
    private String toriaiRetsuNo; // dùng cho cột nào
    private String toriaiRetsuNoIndex; // đánh số thứ tự
    private String toriaiUsedRetsuNo; // được dùng lại cho cột nào
    private String toriaiUsedRetsuNoIndex; // đánh số thứ tự
    private BranchBean branch;
    private MaterialTypeBean materialType;
}

package com.duykypaul.wmanage_api.beans;

import lombok.*;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class ToriaiGyoBean extends BaseBean<ToriaiGyoBean>{
    private String toriaiHeadNo;
    private Integer gyoNo;
    private Integer length;
    private Integer quantity;

    private ConsignmentBean consignment;

    public static ToriaiGyoBean Clone(ToriaiGyoBean toriaiGyoBean) {
        return ToriaiGyoBean.builder()
            .id(toriaiGyoBean.getId())
            .isDeleted(toriaiGyoBean.isDeleted())
            .createdAt(toriaiGyoBean.getCreatedAt())
            .createdBy(toriaiGyoBean.getCreatedBy())
            .modifiedAt(toriaiGyoBean.getModifiedAt())
            .modifiedBy(toriaiGyoBean.getModifiedBy())
            .toriaiHeadNo(toriaiGyoBean.getToriaiHeadNo())
            .gyoNo(toriaiGyoBean.getGyoNo())
            .length(toriaiGyoBean.getLength())
            .quantity(toriaiGyoBean.getQuantity())
            .consignment(toriaiGyoBean.getConsignment())
            .build();
    }
}

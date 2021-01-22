package com.duykypaul.wmanage_api.beans;

import lombok.*;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class ToriaiGyoBean extends BaseBean<ToriaiGyoBean> {
    private String toriaiHeadNo;
    private Integer gyoNo;
    private Integer length;
    private Integer quantity;
}

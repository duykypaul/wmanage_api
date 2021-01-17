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
    private String gyoNo;
    private Integer length;
    private Integer quantity;
    private ToriaiHeadBean toriaiHead;
}

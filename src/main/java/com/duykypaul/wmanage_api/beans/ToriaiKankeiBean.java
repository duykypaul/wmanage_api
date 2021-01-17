package com.duykypaul.wmanage_api.beans;

import lombok.*;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class ToriaiKankeiBean extends BaseBean<ToriaiKankeiBean> {
    private Integer length;
    private ToriaiHeadBean toriaiHead;
    private ToriaiRetsuBean toriaiRetsu;
    private ToriaiGyoBean toriaiGyo;
}

package com.duykypaul.wmanage_api.beans;


import com.duykypaul.wmanage_api.common.Constant.AUTH.ROLE;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RoleBean {
    private Long id;
    private ROLE name;
}

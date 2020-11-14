package com.duykypaul.wmanage_api.beans;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MaterialTypeBean extends BaseBean<MaterialTypeBean> {
    private String materialType;
    private String materialTypeName;
    private String dimension;
}

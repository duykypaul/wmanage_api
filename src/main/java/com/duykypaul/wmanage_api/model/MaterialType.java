package com.duykypaul.wmanage_api.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "material_type")
public class MaterialType extends BaseEntity {
    private String materialType;
    private String dimension;

    public MaterialType(String materialType, String dimension) {
        super();
        this.materialType = materialType;
        this.dimension = dimension;
    }
}

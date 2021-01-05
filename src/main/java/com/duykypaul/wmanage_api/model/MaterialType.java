package com.duykypaul.wmanage_api.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "material_type")
public class MaterialType extends BaseEntity {
    //A, B
    private String materialType;
    // Good, Medium
    private String materialTypeName;
    //6X15X15
    private String dimension;
}

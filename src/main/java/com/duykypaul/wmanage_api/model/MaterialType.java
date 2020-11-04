package com.duykypaul.wmanage_api.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Getter
@Setter
@Table(name = "material_type", uniqueConstraints = {
    @UniqueConstraint(columnNames = "material_type"),
    @UniqueConstraint(columnNames = "Dimension")
})
public class MaterialType extends BaseEntity {
    private String materialType;
    private String Dimension;
}

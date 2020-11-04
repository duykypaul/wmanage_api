package com.duykypaul.wmanage_api.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "cutter_material")
public class CutterMaterial extends BaseEntity {
    private String cutterMaterialNo;
    private Integer length;
    private Integer quantity;
    private Integer lengthUsed;
    private Integer lengthRemaining;

    @ManyToOne
    @JsonManagedReference
    @JoinColumn(name = "material_id")
    private Material material;

    @ManyToOne
    @JsonManagedReference
    @JoinColumn(name = "cutter_head_id")
    private CutterHead cutterHead;
}

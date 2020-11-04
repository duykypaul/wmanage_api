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
@Table(name = "material")
public class Material extends BaseEntity {
    private String materialNo;
    private Integer length;
    private boolean isVisible;
    private String cutterHeadNo;
    private String cutterHeadNoUsed;
    private String cutterLineNo;
    private String cutterMaterialNo;
    private String cutterMaterialNoIndex;
    private String cutterUsedMaterialNo;
    private String cutterUsedMaterialNoIndex;

    @ManyToOne
    @JsonManagedReference
    @JoinColumn(name = "branch_id")
    private Branch branch;

    @ManyToOne
    @JsonManagedReference
    @JoinColumn(name = "material_type_id")
    private MaterialType materialType;
}

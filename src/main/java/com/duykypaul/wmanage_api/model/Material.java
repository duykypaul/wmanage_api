package com.duykypaul.wmanage_api.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "material", uniqueConstraints=@UniqueConstraint(columnNames="materialNo"))
public class Material extends BaseEntity {
    private String materialNo;
    private Integer length;
    private String status;// active, inactive, plan
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

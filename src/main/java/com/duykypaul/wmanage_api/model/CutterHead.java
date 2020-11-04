package com.duykypaul.wmanage_api.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "cutter_head")
public class CutterHead extends BaseEntity {
    private String cutterHeadNo;

    @ManyToOne
    @JsonManagedReference
    @JoinColumn(name = "branch_id")
    private Branch branch;

    @ManyToOne
    @JsonManagedReference
    @JoinColumn(name = "material_type_id")
    private MaterialType materialType;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "cutterHead")
//    @JoinColumn(name = "cutter_head_id") // we need to duplicate the physical information
    private Set<Consignment> consignments;
}

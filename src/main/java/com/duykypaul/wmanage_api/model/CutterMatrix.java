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
@Table(name = "cutter_matrix")
public class CutterMatrix extends BaseEntity {
    private String cutterMatrixNo;
    private Integer quantity;

    @ManyToOne
    @JsonManagedReference
    @JoinColumn(name = "cutter_line_id")
    private CutterLine CutterLine;

    @ManyToOne
    @JsonManagedReference
    @JoinColumn(name = "cutter_material_id")
    private CutterMaterial cutterMaterial;

    @ManyToOne
    @JsonManagedReference
    @JoinColumn(name = "cutter_head_id")
    private CutterHead cutterHead;
}

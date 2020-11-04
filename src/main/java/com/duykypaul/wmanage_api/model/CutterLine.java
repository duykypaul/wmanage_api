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
@Table(name = "cutter_line")
public class CutterLine extends BaseEntity {
    private Integer length;
    private Integer quantity;

    @ManyToOne
    @JsonManagedReference
    @JoinColumn(name = "cutter_head_id")
    private CutterHead cutterHead;

    @ManyToOne
    @JsonManagedReference
    @JoinColumn(name = "consignment_id")
    private Consignment consignment;
}

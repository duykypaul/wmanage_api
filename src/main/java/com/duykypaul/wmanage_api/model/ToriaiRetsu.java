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
@Table(name = "toriai_retsu")
public class ToriaiRetsu extends BaseEntity {
    private String retsuNo;
    private Integer length;
    private Integer quantity;
    private Integer lengthUsed;
    private Integer lengthRemaining;
    private String listMaterialNo;

    @ManyToOne
    @JsonManagedReference
    @JoinColumn(name = "toriai_head_id")
    private ToriaiHead toriaiHead;
}

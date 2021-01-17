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
@Table(name = "toriai_kankei")
public class ToriaiKankei extends BaseEntity {
    private Integer quantity;

    @ManyToOne
    @JsonManagedReference
    @JoinColumn(name = "toriai_gyo_id")
    private ToriaiGyo ToriaiGyo;

    @ManyToOne
    @JsonManagedReference
    @JoinColumn(name = "toriai_retsu_id")
    private ToriaiRetsu toriaiRetsu;

    @ManyToOne
    @JsonManagedReference
    @JoinColumn(name = "toriai_head_id")
    private ToriaiHead toriaiHead;
}

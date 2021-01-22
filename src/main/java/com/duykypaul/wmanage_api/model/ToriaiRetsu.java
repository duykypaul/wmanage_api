package com.duykypaul.wmanage_api.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "toriai_retsu")
public class ToriaiRetsu extends BaseEntity {
    private String toriaiHeadNo;
    private Integer retsuNo;
    private Integer length;
    private Integer quantity;

    private Integer lengthUsed;
    private Integer lengthRemaining;
    private String bozaimotoToriaiHeadNo;
    private String listMaterialNo;
}

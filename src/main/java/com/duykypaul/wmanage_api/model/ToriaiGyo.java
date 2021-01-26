package com.duykypaul.wmanage_api.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Where;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "toriai_gyo")
public class ToriaiGyo extends BaseEntity {
    private String toriaiHeadNo;
    private Integer gyoNo;
    private Integer length;
    private Integer quantity;

    @OneToOne
    @JoinColumn(name = "consignment_id")
    @Where(clause = "is_deleted = false")
    private Consignment consignment;
}

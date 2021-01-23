package com.duykypaul.wmanage_api.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "toriai_gyo")
@JsonIdentityInfo(generator= ObjectIdGenerators.UUIDGenerator.class, property="@id")
public class ToriaiGyo extends BaseEntity {
    private String toriaiHeadNo;
    private Integer gyoNo;
    private Integer length;
    private Integer quantity;

    @OneToOne
    @JoinColumn(name = "consignment_id")
    private Consignment consignment;
}

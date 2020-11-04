package com.duykypaul.wmanage_api.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "consignment_details", uniqueConstraints = {
    @UniqueConstraint(columnNames = "length")
})
public class ConsignmentDetail extends BaseEntity {
    private Integer length;
    private Integer quantity;

    @ManyToOne
    @JsonManagedReference
    @JoinColumn(name = "consignment_id")
    private Consignment consignment;
}

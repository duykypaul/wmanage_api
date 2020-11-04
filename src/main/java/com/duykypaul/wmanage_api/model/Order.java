package com.duykypaul.wmanage_api.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "orders")
public class Order extends BaseEntity {
    @NotBlank
    @Size(max = 120)
    private String customer;
    private String deliveryAddress;
    private Date expectedDeliveryDate;
    private Integer numberStack;

    @ManyToOne
    @JsonManagedReference
    @JoinColumn(name = "branch_id")
    private Branch branch;
}

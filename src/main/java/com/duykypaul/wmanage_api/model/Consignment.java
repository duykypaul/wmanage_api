package com.duykypaul.wmanage_api.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;

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
@Table(name = "consignments")
public class Consignment extends BaseEntity {
    @NotBlank
    @Size(max = 120)
    private String customer;
    private String deliveryAddress;
    private Date expectedDeliveryDate;
    private Integer numberStack;

    @ManyToOne
    @JsonManagedReference
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne
    @JsonManagedReference
    @JoinColumn(name = "material_type_id")
    private MaterialType materialType;

    @ManyToOne
    @JoinColumn(name = "cutter_head_id", insertable = false, updatable = false) // thông qua khóa ngoại cutter_head_id
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private CutterHead cutterHead;
}

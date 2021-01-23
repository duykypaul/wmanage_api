package com.duykypaul.wmanage_api.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "consignments")
@JsonIdentityInfo(generator= ObjectIdGenerators.UUIDGenerator.class, property="@id")
public class Consignment extends BaseEntity {
    /*
     * quy tắc sinh chuỗi {materialTypeCode}{dimension}N{orderID}
     */
    @NotBlank
    private String consignmentNo;

    @Size(max = 44)
    private String customer;
    @Size(max = 44)
    private String deliveryAddress;
    private Integer length;
    private Integer quantity;
    private String status;

    @Temporal(TemporalType.DATE)
    @JsonFormat(pattern="yyyy-MM-dd")
    private Date expectedDeliveryDate;

    @ManyToOne(cascade = CascadeType.ALL)
    @JsonBackReference
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "material_type_id")
    private MaterialType materialType;

    @OneToOne(mappedBy = "consignment")
    private ToriaiGyo toriaiGyo;
}

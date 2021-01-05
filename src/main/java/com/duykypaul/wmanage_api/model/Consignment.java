package com.duykypaul.wmanage_api.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "consignments")
public class Consignment extends BaseEntity {
    /*
     * quy tắc sinh chuỗi {materialTypeCode}1{dimension}9
     */
    @NotBlank
    @Size(max = 10)
    private String consignmentNo;

    @Size(max = 120)
    private String customer;
    private String deliveryAddress;
    private Integer length;
    private Integer quantity;

    @Temporal(TemporalType.DATE)
    @JsonFormat(pattern="yyyy-MM-dd")
    private Date expectedDeliveryDate;

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "material_type_id")
    private MaterialType materialType;

    @ManyToOne
    @JoinColumn(name = "cutter_head_id", insertable = false, updatable = false) // thông qua khóa ngoại cutter_head_id
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private CutterHead cutterHead;
}

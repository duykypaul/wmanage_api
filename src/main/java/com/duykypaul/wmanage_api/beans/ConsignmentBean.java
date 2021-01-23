package com.duykypaul.wmanage_api.beans;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;
import java.util.Date;

@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class ConsignmentBean extends BaseBean<ConsignmentBean> implements Serializable {
    private String consignmentNo;
    private String customer;
    private String deliveryAddress;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd")
    private Date expectedDeliveryDate;
    private Integer length;
    private Integer quantity;
    private String status;

//    @JsonBackReference
    private OrderBean order;

//    @JsonBackReference
    private MaterialTypeBean materialType;

    @JsonIgnore
    private ToriaiGyoBean toriaiGyo;
}

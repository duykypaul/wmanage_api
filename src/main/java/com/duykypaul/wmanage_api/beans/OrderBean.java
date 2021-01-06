package com.duykypaul.wmanage_api.beans;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.Date;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class OrderBean extends BaseBean<OrderBean> {
    private String customer;
    private String deliveryAddress;
    private String status;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date deliveryDate;

//    @JsonBackReference
    private BranchBean branch;

//    @JsonManagedReference
    private List<ConsignmentBean> consignments;
}

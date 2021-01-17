package com.duykypaul.wmanage_api.beans;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.Date;

@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class ToriaiHeadBean extends BaseBean<ToriaiHeadBean> {
    private String toriaiHeadNo;
    private String status;
    //enum TYPE_TORIAI
    private String typeToriai;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date machiningCompletionDate;

    private Integer totalLengthExpected;
    private Integer totalQuantity;
    private Integer totalLengthRemain;
    private Integer rateUse;
    private Integer rateRemain;

//    @JsonBackReference
    private BranchBean branch;

    private MaterialTypeBean materialType;
}

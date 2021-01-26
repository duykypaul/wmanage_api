package com.duykypaul.wmanage_api.beans;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
    private Integer totalLengthUsed;
    private Integer totalLengthRemain;
    private Integer rateUse;
    private Integer rateRemain;

//    @JsonBackReference
    private BranchBean branch;
    private MaterialTypeBean materialType;

    private List<ToriaiRetsuBean> listToriaiRetsu;
    private List<ToriaiGyoBean> listToriaiGyo;
    private List<ToriaiKankeiBean> listToriaiKankei;

    private Integer[][] algorithmResult;

    // show message on screen when toriai error
    List<String> message = new ArrayList<>();
    List<String> toriaiHeadNos = new ArrayList<>();

}

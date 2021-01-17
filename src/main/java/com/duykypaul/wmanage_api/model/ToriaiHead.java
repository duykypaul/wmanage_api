package com.duykypaul.wmanage_api.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "toriai_head")
public class ToriaiHead extends BaseEntity {
    private String toriaiHeadNo;
    private String status;
    //enum TYPE_TORIAI
    private String typeToriai;

    @Temporal(TemporalType.DATE)
    @JsonFormat(pattern="yyyy-MM-dd")
    private Date machiningCompletionDate;

    private Integer totalLengthExpected;
    private Integer totalQuantity;
    private Integer totalLengthUsed;
    private Integer totalLengthRemain;
    private Integer rateUse;
    private Integer rateRemain;

    @ManyToOne
    @JsonManagedReference
    @JoinColumn(name = "branch_id")
    private Branch branch;

    @ManyToOne
    @JsonManagedReference
    @JoinColumn(name = "material_type_id")
    private MaterialType materialType;
}

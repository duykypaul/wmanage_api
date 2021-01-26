package com.duykypaul.wmanage_api.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import javax.validation.constraints.Size;

@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "material", uniqueConstraints=@UniqueConstraint(columnNames={"materialNo", "seiKbn", "toriaiHeadNo"}))
public class Material extends BaseEntity {
    // quy tắc sinh {seikbn}{branchCode}{8}(11) or {seikbn}{branchCode}{8}{phaseCode}_autoCrement 01->99 (14)
    @Size(max = 14)
    private String materialNo; // kanrino
    private String seiKbn; // kanrino
    private Integer length;
    private String status;// active, inactive, plan
    private String toriaiHeadNo; // sinh ra
    private String toriaiHeadNoUsed; // duoc su dung boi
    private Integer toriaiGyoNo; // dùng cho hàng nào
    private Integer toriaiRetsuNo; // dùng cho cột nào
    private Integer toriaiRetsuNoIndex; // đánh số thứ tự
    private Integer toriaiUsedRetsuNo; // được dùng lại cho cột nào
    private Integer toriaiUsedRetsuNoIndex; // đánh số thứ tự

    @ManyToOne
    @JsonManagedReference
    @JoinColumn(name = "branch_id")
    private Branch branch;

    @ManyToOne
    @JsonManagedReference
    @JoinColumn(name = "material_type_id")
    private MaterialType materialType;
}

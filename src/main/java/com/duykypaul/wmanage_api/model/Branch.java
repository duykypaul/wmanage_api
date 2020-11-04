package com.duykypaul.wmanage_api.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Getter
@Setter
@Table(name = "branch", uniqueConstraints = {
    @UniqueConstraint(columnNames = "branch_code"),
    @UniqueConstraint(columnNames = "branch_name")
})
public class Branch extends BaseEntity {
    private String branchCode;
    private String branchName;
    private Integer wattage;
}

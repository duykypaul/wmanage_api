package com.duykypaul.wmanage_api.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "branch", uniqueConstraints = {
    @UniqueConstraint(columnNames = "branchCode"),
    @UniqueConstraint(columnNames = "branchName")
})
public class Branch extends BaseEntity {
    private String branchCode;
    private String branchName;
    private Integer wattage;

    public Branch(String branchCode, String branchName, Integer wattage) {
        this.branchCode = branchCode;
        this.branchName = branchName;
        this.wattage = wattage;
    }
}

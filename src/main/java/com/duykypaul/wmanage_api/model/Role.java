package com.duykypaul.wmanage_api.model;

import com.duykypaul.wmanage_api.common.Constant.AUTH.ROLE;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(name = "roles")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private ROLE name;

    public Role() {
    }

    public Role(ROLE name) {
        this.name = name;
    }
}

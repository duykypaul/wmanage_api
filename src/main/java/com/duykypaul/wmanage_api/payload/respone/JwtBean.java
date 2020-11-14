package com.duykypaul.wmanage_api.payload.respone;

import com.duykypaul.wmanage_api.beans.UserBean;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JwtBean {
    private int status;
    private UserBean user;
    private String token;
    private String type = "Bearer";

    public JwtBean(int status, String token, UserBean user) {
        this.token = token;
        this.user = user;
        this.status = status;
    }
}

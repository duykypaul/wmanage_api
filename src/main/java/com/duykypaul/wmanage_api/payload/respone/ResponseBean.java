package com.duykypaul.wmanage_api.payload.respone;

import lombok.Data;

@Data
public class ResponseBean {
    private int status;
    private Object data;
    private String message;

    public ResponseBean(int status, Object data, String message) {
        this.status = status;
        this.data = data;
        this.message = message;
    }
}

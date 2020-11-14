package com.duykypaul.wmanage_api.payload.respone;

import lombok.Data;

@Data
public class ResponseBean {
    private int status;
    private Object body;
    private String message;

    public ResponseBean(int status, Object data, String message) {
        this.status = status;
        this.body = data;
        this.message = message;
    }
}

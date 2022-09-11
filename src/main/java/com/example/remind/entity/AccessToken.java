package com.example.remind.entity;

import com.alibaba.fastjson2.annotation.JSONField;
import lombok.Data;

@Data
public class AccessToken {

    @JSONField(name = "access_token")
    private String access_token;

    @JSONField(name = "expires_in")
    private Long expires_in;

    public void format_expires_in() {
        this.expires_in = this.expires_in * 1000 + System.currentTimeMillis();
    }

    public boolean isExpired() {
        return System.currentTimeMillis() > this.expires_in;
    }
}

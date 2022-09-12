package com.example.remind.entity;

import com.example.remind.utils.utils;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class TemplateMessage {

    private String touser;

    private String template_id;

    private String url;

    private String client_msg_id;

    private Map<String, String> miniprogram = new HashMap<>();

    private Map<String, Map<String, String>> data = new HashMap<>();

    {
        miniprogram.put("appid", "");
        miniprogram.put("pagepath", "");
        url = "";
        client_msg_id = "";
        put("time", utils.getTime(), "#FF312C");
    }

    public void put(String name, String value, String color) {
        HashMap<String, String> map = new HashMap<>();
        map.put("value", value);
        map.put("color", color);
        this.data.put(name, map);
    }

}

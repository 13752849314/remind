package com.example.remind.entity;

import com.alibaba.fastjson2.annotation.JSONField;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AbstractButton {

    @JSONField(name = "type")
    private String type;

    @JSONField(name = "name")
    private String name;

    @JSONField(name = "key")
    private String key;

}

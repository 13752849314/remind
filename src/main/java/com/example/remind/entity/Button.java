package com.example.remind.entity;

import com.alibaba.fastjson2.annotation.JSONField;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class Button {

    @JSONField(name = "button")
    private List<AbstractButton> button = new ArrayList<>();

}

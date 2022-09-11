package com.example.remind.utils;

import com.alibaba.fastjson2.JSON;
import com.example.remind.entity.Button;
import com.example.remind.service.WxService;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class CreateMenu {
    public static void creatMenu(WxService wxService) {
        InputStream is = ClassLoader.getSystemResourceAsStream("./json/menu.json");
        try {
            if (is != null) {
                String s = IOUtils.toString(is, StandardCharsets.UTF_8);
                Button button = JSON.parseObject(s, Button.class);
                String url = "https://api.weixin.qq.com/cgi-bin/menu/create?access_token=ACCESS_TOKEN";
                url = url.replace("ACCESS_TOKEN", wxService.getAccessToken());
                String result = utils.post(url, s);
                System.out.println(result);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

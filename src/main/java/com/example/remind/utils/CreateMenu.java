package com.example.remind.utils;

import com.alibaba.fastjson2.JSON;
import com.example.remind.entity.Button;
import com.example.remind.service.WxService;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class CreateMenu {

    /**
     * 创建菜单
     *
     * @param wxService 微信
     */
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

    /**
     * 设置行业
     *
     * @param wxService 微信
     */
    public static void setIndustry(WxService wxService) {
        String url = "https://api.weixin.qq.com/cgi-bin/template/api_set_industry?access_token=ACCESS_TOKEN";
        url = url.replace("ACCESS_TOKEN", wxService.getAccessToken());
        Map<String, String> data = new HashMap<>();
        data.put("industry_id1", "17");
        data.put("industry_id2", "2");
        String post = utils.post(url, data.toString());
        System.out.println(post);
    }
}

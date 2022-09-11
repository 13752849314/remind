package com.example.remind.service;

import com.alibaba.fastjson2.JSONObject;
import com.example.remind.utils.utils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

@Service
public class WeatherService {

    @Value("${city}")
    private String location;

    @Value("${JUHE.API_URL}")
    private String API_URL;

    @Value("${JUHE.API_KEY}")
    private String API_KEY;

    public String getWeather() {
        HashMap<String, String> map = new HashMap<>();
        map.put("city", location);
        map.put("key", API_KEY);
        String url = getFullUrl(map);
        String s = utils.get(url);
        JSONObject jsonObject = JSONObject.parseObject(s);
        if (jsonObject != null) {
            if (jsonObject.getInteger("error_code") == 0) { //查询成功
                JSONObject result = jsonObject.getJSONObject("result");
                return resultToStr(result);
            }
        }
        return "查询失败！";
    }

    private String resultToStr(JSONObject result) {
        StringBuilder builder = new StringBuilder();
        String city = result.getString("city");
        builder.append("当前城市").append(city).append("\n");
        JSONObject realtime = result.getJSONObject("realtime");
        builder.append("天气：").append(realtime.getString("info")).append("\n");
        builder.append("温度：").append(realtime.getString("temperature")).append("度\n");
        builder.append("湿度：").append(realtime.getString("humidity")).append("\n");
        builder.append("风向：").append(realtime.getString("direct")).append("\n");
        builder.append("风力：").append(realtime.getString("power")).append("\n");
        builder.append("空气质量指数：").append(realtime.getString("aqi")).append("\n");
        return builder.toString();
    }

    private String getFullUrl(HashMap<String, String> map) {
        StringBuilder builder = new StringBuilder();
        builder.append(API_URL);
        builder.append("?");
        for (Map.Entry<String, String> i : map.entrySet()) {
            builder.append(i.getKey());
            builder.append("=");
            try {
                builder.append(URLEncoder.encode(i.getValue(), "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            builder.append("&");
        }
        builder.delete(builder.length() - 1, builder.length());
        return builder.toString();
    }

}

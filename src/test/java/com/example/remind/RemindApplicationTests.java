package com.example.remind;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.example.remind.entity.AbstractButton;
import com.example.remind.entity.Button;
import com.example.remind.entity.Course;
import com.example.remind.entity.User;
import com.example.remind.mapper.CourseMapper;
import com.example.remind.mapper.UserMapper;
import com.example.remind.service.PunchService;
import com.example.remind.service.WeatherService;
import com.example.remind.service.WxService;
import com.example.remind.utils.CreateMenu;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;

@SpringBootTest
class RemindApplicationTests {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private CourseMapper courseMapper;

    @Autowired
    private WxService wxService;

    @Autowired
    private WeatherService weatherService;

    @Autowired
    PunchService punchService;

    @Test
    void contextLoads() {
        String accessToken = wxService.getAccessToken();
        System.out.println(accessToken);
        System.out.println(wxService.getAccessToken());

        System.out.println("=-----------------------=");

        InputStream is = ClassLoader.getSystemResourceAsStream("./json/menu.json");
        try {
            if (is != null) {
                String s = IOUtils.toString(is, StandardCharsets.UTF_8);
                Button button = JSON.parseObject(s, Button.class);
                System.out.println(button);
                button.getButton().add(new AbstractButton("click", "KUST", "xxx"));

                System.out.println(JSONObject.toJSONString(button));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("=------------------------------=");

//        CreateMenu.creatMenu(wxService);

        System.out.println("=--------------------------------=");

        String weather = weatherService.getWeather();
        System.out.println(weather);

        System.out.println("=-------------------------------=");

//        CreateMenu.setIndustry(wxService);

        CreateMenu.getIndustry(wxService);

        System.out.println("=---------------------------------=");
        System.out.println(wxService.getUserList());

        System.out.println("=------------------------------------=");

//        punchService.HealthPunch();

    }

}

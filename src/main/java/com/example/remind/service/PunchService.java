package com.example.remind.service;

import com.alibaba.fastjson2.JSONObject;
import com.example.remind.entity.TemplateMessage;
import com.example.remind.utils.utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PunchService {

    @Autowired
    private WxService wxService;

    @Value("${URL.sendTemplate}")
    private String SendTemplateURL;

    // 模板ID
    private static final String Template_ID = "KNmABCsj4IlnHp9Puf3geOXZJ7mPkCyliKoVBLeIREM";
    private static final String health_MESSAGE = "又是新的一天！记得健康打卡哦！";
    private static final String morning_MESSAGE = "晨检打卡开始了！记得打卡哦！\n 截止时间: 10:00";
    private static final String lunch_MESSAGE = "午检打卡开始了！记得打卡哦！\n 截止时间: 15:00";
    private static final String night_MESSAGE = "晚检打卡开始了！记得打卡哦！\n 截止时间: 23:00";


    private void sendTemplateMessage(TemplateMessage text) {
        String url = SendTemplateURL.replace("ACCESS_TOKEN", wxService.getAccessToken());
        String data = JSONObject.toJSONString(text);
        String result = utils.post(url, data);
        System.out.println(result);
    }


    private void send(String msg) {
        List<String> users = wxService.getUserList();
        TemplateMessage message = new TemplateMessage();
        message.setTemplate_id(Template_ID);
        message.put("message", msg, "#28FF17");
        for (String user : users) {
            message.setTouser(user);
            sendTemplateMessage(message);
        }
    }

    /**
     * 健康打卡提醒
     */
    @Scheduled(cron = "0 0 8 * * ?")
    public void HealthPunch() {
        send(health_MESSAGE);
    }

    /**
     * 晨检打卡提醒
     */
    @Scheduled(cron = "0 30 7 * * ?")
    public void MorningPunch() {
        send(morning_MESSAGE);
    }

    /**
     * 晨检打卡提醒
     */
    @Scheduled(cron = "0 0 12 * * ?")
    public void LunchPunch() {
        send(lunch_MESSAGE);
    }

    /**
     * 晨检打卡提醒
     */
    @Scheduled(cron = "0 0 22 * * ?")
    public void NightPunch() {
        send(night_MESSAGE);
    }
}

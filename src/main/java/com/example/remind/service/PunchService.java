package com.example.remind.service;

import com.alibaba.fastjson2.JSONObject;
import com.example.remind.entity.TemplateMessage;
import com.example.remind.entity.User;
import com.example.remind.mapper.UserMapper;
import com.example.remind.utils.utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Component
public class PunchService {

    @Autowired
    private WxService wxService;

    @Autowired
    private UserMapper userMapper;

    @Value("${URL.sendTemplate}")
    private String SendTemplateURL;

    // 模板ID
    private static final String Template_ID = "KNmABCsj4IlnHp9Puf3geOXZJ7mPkCyliKoVBLeIREM";
    private static final String health_MESSAGE = "又是新的一天！记得健康打卡哦！";
    private static final String morning_MESSAGE = "晨检打卡开始了！记得打卡哦！\n截止时间: 10:00";
    private static final String lunch_MESSAGE = "午检打卡开始了！记得打卡哦！\n截止时间: 15:00";
    private static final String night_MESSAGE = "晚检打卡开始了！记得打卡哦！\n截止时间: 23:00";


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
            System.out.println("发送成功！");
        }
    }

    /**
     * 健康打卡提醒
     */
    @Scheduled(cron = "0 0 8 * * ?", zone = "Asia/Shanghai")
    public void HealthPunch() {
        send(health_MESSAGE);
    }

    /**
     * 晨检打卡提醒
     */
    @Scheduled(cron = "0 30 7 * * ?", zone = "Asia/Shanghai")
    public void MorningPunch() {
        send(morning_MESSAGE);
    }

    /**
     * 晨检打卡提醒
     */
    @Scheduled(cron = "0 0 12 * * ?", zone = "Asia/Shanghai")
    public void LunchPunch() {
        send(lunch_MESSAGE);
    }

    /**
     * 晨检打卡提醒
     */
    @Scheduled(cron = "0 0 22 * * ?", zone = "Asia/Shanghai")
    public void NightPunch() {
        send(night_MESSAGE);
    }


    // @Scheduled(cron = "0/10 * * * * ? ")
// public void test() {
//     System.out.println("test");
//     send(night_MESSAGE);
// }
//    @Scheduled(cron = "0/30 * * * * ?", zone = "Asia/Shanghai")
//    public void myTest() {
//        List<String> users = wxService.getUserList();
//        System.out.println(users);
//        System.out.println("test:" + utils.getTime());
//        for (String user : users) {
//            User uu = userMapper.getUserByOpenId(user);
//            if (uu.getUsername().equals("敖鸥")) {
//                TemplateMessage message = new TemplateMessage();
//                message.setTemplate_id(Template_ID);
//                message.setTouser(user);
//                message.put("message", "这是一个测试!", "#28FF17");
//                sendTemplateMessage(message);
//            }
//        }
//    }
}

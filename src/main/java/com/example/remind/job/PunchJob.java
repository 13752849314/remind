package com.example.remind.job;

import com.example.remind.entity.User;
import com.example.remind.mapper.UserMapper;
import com.example.remind.service.MailService;
import com.example.remind.utils.utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import java.util.List;

@Component
public class PunchJob {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private MailService mailService;


    /**
     * 每天早晨7:30
     */
    @Scheduled(cron = "0 30 7 * * ?")
    public void morningPunch() {
        List<User> users = userMapper.selectList(null);
        for (User i : users) {
            if (i.getRemindType() == 0) {
                System.out.println(i.getRemindType() + "  message: " + PunchType.morning.getMessage());
                String s = utils.RemindStringHTML(i.getUsername(), PunchType.morning.getMessage());
                try {
                    mailService.sendHTMLMail(i.getEmail(), PunchType.morning.getType(), s, true);
                } catch (MessagingException e) {
                    e.printStackTrace();
                }
            } else {
                System.out.println(i.getRemindType() + "  message: " + PunchType.morning.getMessage());
            }
        }
    }

    /**
     * 每天中午12:00
     */
    @Scheduled(cron = "0 0 12 * * ?")
    public void noonPunch() {
        List<User> users = userMapper.selectList(null);
        for (User i : users) {
            if (i.getRemindType() == 0) {
                System.out.println(i.getRemindType() + "  message: " + PunchType.lunch.getMessage());
                String s = utils.RemindStringHTML(i.getUsername(), PunchType.lunch.getMessage());
                try {
                    mailService.sendHTMLMail(i.getEmail(), PunchType.lunch.getType(), s, true);
                } catch (MessagingException e) {
                    e.printStackTrace();
                }
            } else {
                System.out.println(i.getRemindType() + "  message: " + PunchType.lunch.getMessage());
            }
        }
    }

    /**
     * 每天晚上22:00
     */
    @Scheduled(cron = "0 0 22 * * ?")
    public void nightPunch() {
        List<User> users = userMapper.selectList(null);
        for (User i : users) {
            if (i.getRemindType() == 0) {
                System.out.println(i.getRemindType() + "  message: " + PunchType.night.getMessage());
                String s = utils.RemindStringHTML(i.getUsername(), PunchType.night.getMessage());
                try {
                    mailService.sendHTMLMail(i.getEmail(), PunchType.night.getType(), s, true);
                } catch (MessagingException e) {
                    e.printStackTrace();
                }
            } else {
                System.out.println(i.getRemindType() + "  message: " + PunchType.night.getMessage());
            }
        }
    }

    /**
     * 每天早上9:00
     */
    @Scheduled(cron = "0 0 9 * * ?")
    public void healthyPunch() {
        List<User> users = userMapper.selectList(null);
        for (User i : users) {
            if (i.getRemindType() == 0) {
                System.out.println(i.getRemindType() + "  message: " + PunchType.healthy.getMessage());
                String s = utils.RemindStringHTML(i.getUsername(), PunchType.healthy.getMessage());
                try {
                    mailService.sendHTMLMail(i.getEmail(), PunchType.healthy.getType(), s, true);
                } catch (MessagingException e) {
                    e.printStackTrace();
                }
            } else {
                System.out.println(i.getRemindType() + "  message: " + PunchType.healthy.getMessage());
            }
        }
    }
}


enum PunchType {
    morning("晨检打卡提醒!", "晨检时间段为  7:30-10:00!"),
    lunch("午检打卡提醒!", "午检时间段为  12:00-13:00!"),
    night("晚检打卡提醒!", "晚检时间段为  22:00-23:00!"),
    healthy("健康打卡!", "又是新的一天，记得健康打卡哦!");

    private String Type;
    private String message;

    PunchType() {
    }

    PunchType(String Type, String message) {
        this.Type = Type;
        this.message = message;
    }

    public String getType() {
        return Type;
    }

    public String getMessage() {
        return message;
    }

    public void setType(String type) {
        Type = type;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

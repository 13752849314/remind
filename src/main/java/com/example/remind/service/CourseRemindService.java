package com.example.remind.service;

import com.example.remind.entity.Course;
import com.example.remind.entity.User;
import com.example.remind.mapper.CourseMapper;
import com.example.remind.mapper.UserMapper;
import com.example.remind.utils.utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.util.*;

@Service
public class CourseRemindService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private MailService mailService;

    @Autowired
    private CourseMapper courseMapper;

    public void getUser() {
        List<User> users = userMapper.selectList(null);
        System.out.println(users);
    }

    public void sendMail() {
        String to = "371575373@qq.com";
        String subject = "371575373@qq.com";
        String content = utils.RemindStringHTML("123", "该打卡了");
        try {
            mailService.sendHTMLMail(to, subject, content, true);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    public void course() {
        List<Course> courses = courseMapper.selectList(null);
        System.out.println(courses);
    }

    public void dayCoursesRemind() {
        List<User> users = userMapper.selectList(null);
        for (User h : users) {
            List<Course> courses = courseMapper.getCourseByUsername(h.getUsername());
            StringBuilder message = new StringBuilder();
            if (!courses.isEmpty()) {
                Date date = new Date();
                Integer integer = utils.dateGetWeeks(date);
                ArrayList<Course> courses1 = new ArrayList<>();
                Calendar cal = Calendar.getInstance();
                for (Course course : courses) {
                    cal.setTime(course.getStartAt());
                    cal.add(Calendar.DATE, 7 * course.getTotal() + 1);
                    Integer weeks = utils.dateGetWeeks(course.getStartAt());
                    if (integer.equals(weeks) && (course.getStartAt().compareTo(date) <= 0)
                            && (cal.getTime().compareTo(date) > 0)) {
                        System.out.println(course.getCourseName());
                        courses1.add(course);
                    }
                }
                if (!courses1.isEmpty()) {
                    Collections.sort(courses1); // 排序
                    for (Course c1 : courses1) {
                        message.append(c1.getInfo());
                        message.append("\n");
                    }
                }
            }
            if (message.length() > 0 && h.getRemindType() == 0) {
                try {
                    mailService.sendHTMLMail(h.getEmail(),
                            "今日课程提醒!",
                            utils.RemindStringHTML(h.getUsername(), message.toString()),
                            true);
                } catch (MessagingException e) {
                    e.printStackTrace();
                }
            } else {
                if (h.getRemindType() == 0) {
                    try {
                        mailService.sendHTMLMail(h.getEmail(),
                                "今日课程提醒!",
                                utils.RemindStringHTML(h.getUsername(), "今日无课，尽情happy吧!"),
                                true);
                    } catch (MessagingException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}

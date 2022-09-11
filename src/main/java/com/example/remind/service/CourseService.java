package com.example.remind.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.remind.entity.Course;
import com.example.remind.entity.User;
import com.example.remind.mapper.CourseMapper;
import com.example.remind.mapper.UserMapper;
import com.example.remind.utils.utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Service
public class CourseService {

    @Autowired
    UserMapper userMapper;

    @Autowired
    CourseMapper courseMapper;

    public String getCoursesByOpenId(String openId) {
        // 找到openId 对应的用户
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.eq("openId", openId);
        User user = userMapper.selectOne(userQueryWrapper);

        String coursesMsg = "";

        if (user == null) {
            coursesMsg = "该用户还没有绑定，请发送[绑定+用户名]进行绑定！";
        } else {
            List<Course> courses = courseMapper.getCourseByUsername(user.getUsername());
            coursesMsg = courseMake(courses);
        }

        return coursesMsg;
    }

    public String courseMake(List<Course> courses) {
        Collections.sort(courses);
        StringBuilder builder = new StringBuilder();
        Date date = new Date();
        int newWeek = utils.dateGetWeeks(date);
        Calendar cal = Calendar.getInstance();
        for (Course course : courses) {
            cal.setTime(course.getStartAt());
            cal.add(Calendar.DATE, 7 * course.getTotal() + 1);
            if (utils.dateGetWeeks(course.getStartAt()) == newWeek && (course.getStartAt().compareTo(date) <= 0)
                    && (cal.getTime().compareTo(date) > 0)) {
                builder.append(course.getInfo());
                builder.append("\n");
            }
        }
        if (builder.length() <= 0) {
            return "今日无课，尽情happy吧！";
        }
        return builder.toString();
    }

}

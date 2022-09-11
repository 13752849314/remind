package com.example.remind.job;

import com.example.remind.entity.User;
import com.example.remind.mapper.CourseMapper;
import com.example.remind.mapper.UserMapper;
import com.example.remind.service.CourseRemindService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
public class CourseJob {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private CourseMapper courseMapper;

    @Autowired
    private CourseRemindService courseRemindService;

//    @Scheduled(cron = "0/30 * * * * ?") //每隔30秒触发
//    public void test() {
//        Date date = new Date();
//        System.out.println(123 + date.toString());
//    }

    /**
     * 早上7:30
     */
    @Scheduled(cron = "0 30 7 * * ?")
    public void courseRemind() {
        courseRemindService.dayCoursesRemind();
    }

//    @Scheduled(cron = "0 0 14 * * ?")
//    public void test1() {
//        courseRemindService.dayCoursesRemind();
//    }
}

package com.example.remind.controller;

import com.example.remind.service.CourseRemindService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RequestMapping("/remind")
@RestController
public class CourseRemindController {

    @Resource
    private CourseRemindService courseRemindService;

    @GetMapping("/course")
    public void CourseRemind() {
        courseRemindService.getUser();
//        courseRemindService.sendMail();
        courseRemindService.course();
    }
}

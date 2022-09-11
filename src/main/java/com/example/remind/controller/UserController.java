package com.example.remind.controller;

import com.example.remind.common.Result;
import com.example.remind.common.ResultInfo;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RequestMapping("/user")
@RestController
public class UserController {

    @PostMapping("/addCourse")
    public Result addCourse(@RequestBody Map<String, Object> map) {
        System.out.println(map);
        return Result.success()
                .codeAndMessage(ResultInfo.SUCCESS)
                .data("123", "321");
    }

}

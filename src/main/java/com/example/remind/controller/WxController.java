package com.example.remind.controller;

import com.example.remind.service.WxService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Map;

@RestController
public class WxController {

    @Autowired
    WxService wxService;

    @GetMapping("/wx")
    public String indexGet(@RequestParam Map<String, Object> map) {
        System.out.println("get");
        if (wxService.check(map)) {
            System.out.println("接入成功!");
            return (String) map.get("echostr");
        } else {
            System.out.println("接入失败!");
        }
        return "get";
    }

    @PostMapping("/wx")
    public String indexPost(HttpServletRequest request) throws IOException {
        System.out.println("post");
        Map<String, String> requestMap = wxService.paresRequest(request.getInputStream());
//        System.out.println(requestMap);

        return wxService.getResponse(requestMap);
    }
}

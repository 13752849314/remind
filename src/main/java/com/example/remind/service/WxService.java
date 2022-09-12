package com.example.remind.service;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.remind.entity.AccessToken;
import com.example.remind.entity.BaseMessage;
import com.example.remind.entity.TextMessage;
import com.example.remind.entity.User;
import com.example.remind.mapper.UserMapper;
import com.example.remind.utils.utils;
import com.thoughtworks.xstream.XStream;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.ServletInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class WxService {

    @Value("${token}")
    private String token;

    @Autowired
    private CourseService courseService;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private WeatherService weatherService;

    @Value("${testWx.AppID}")
    private String appID;

    @Value("${testWx.AppSecret}")
    private String appSecret;

//    @Value("${wx.AppID}")
//    private String appID;
//
//    @Value("${wx.AppSecret}")
//    private String appSecret;

    @Value("${URL.getToken}")
    private String GetTokenURL;

    @Value("${URL.getUserList}")
    private String GetUserListURL;

    private static AccessToken at;

    private void getToken() {
        String url = GetTokenURL.replace("APPID", appID).replace("APPSECRET", appSecret);
        String tokenStr = utils.get(url);
//        System.out.println(tokenStr);
        AccessToken accessToken = JSON.parseObject(tokenStr, AccessToken.class);
        if (accessToken != null) {
            accessToken.format_expires_in();
        }
        at = accessToken;
    }

    public String getAccessToken() {
        if (at == null || at.isExpired()) {
            getToken();
        }
        return at.getAccess_token();
    }

    /**
     * 1）将token、timestamp、nonce三个参数进行字典序排序
     * 2）将三个参数字符串拼接成一个字符串进行sha1加密
     * 3）开发者获得加密后的字符串可与 signature 对比，标识该请求来源于微信
     *
     * @param map 微信所带参数
     * @return 检验是否通过
     */
    public boolean check(Map<String, Object> map) {
        String timestamp = (String) map.get("timestamp");
        String nonce = (String) map.get("nonce");
        String signature = (String) map.get("signature");

        String[] strings = new String[]{token, timestamp, nonce};
        Arrays.sort(strings);

        String string = strings[0] + strings[1] + strings[2];
        String mySig = sha1(string);

        return mySig.equalsIgnoreCase(signature);
    }

    private static String sha1(String str) {
        try {
            // 获取加密对象
            MessageDigest md = MessageDigest.getInstance("sha1");
            // 进行加密
            byte[] digest = md.digest(str.getBytes());
            // 处理加密结果
            char[] chars = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
            StringBuilder sb = new StringBuilder();
            for (byte b : digest) {
                sb.append(chars[(b >> 4) & 15]);
                sb.append(chars[b & 15]);
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 解析XML数据包
     *
     * @param is 输入流
     * @return 键值对
     */
    public Map<String, String> paresRequest(ServletInputStream is) {
        HashMap<String, String> map = new HashMap<>();
        SAXReader reader = new SAXReader();
        try {
            Document document = reader.read(is);
            Element root = document.getRootElement();
            List<Element> elements = root.elements();
            for (Element element : elements) {
                map.put(element.getName(), element.getStringValue());
            }
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        return map;
    }

    /**
     * 处理事件和消息的回复
     *
     * @param requestMap 请求参数
     * @return XML数据包
     */
    public String getResponse(Map<String, String> requestMap) {
        BaseMessage msg = null;
        String msgType = requestMap.get("MsgType");
        switch (msgType) {
            case "text": {
                msg = dealTextMessage(requestMap);
                break;
            }
            case "image": {
                System.out.println("image");
                break;
            }
            case "voice": {
                System.out.println("voice");
                break;
            }
            case "video": {
                System.out.println("video");
                break;
            }
            case "shortvideo": {
                System.out.println("shortvideo");
                break;
            }
            case "location": {
                System.out.println("location");
                break;
            }
            case "link": {
                System.out.println("link");
                break;
            }
            case "event": {
                msg = dealEventMessage(requestMap);
                break;
            }
            default: {
                msg = new TextMessage(requestMap, "你干嘛!");
                break;
            }
        }
//        return stream.toXML(msg);
        return ObjectToXML(msg);
    }

    /**
     * 处理事件消息
     *
     * @param requestMap 请求参数
     * @return TextMessage
     */
    private BaseMessage dealEventMessage(Map<String, String> requestMap) {
        String event = requestMap.get("Event");
        switch (event) {
            case "CLICK": {
                return dealClick(requestMap);
            }
            case "VIEW": {
                return dealView(requestMap);
            }
            default: {

                break;
            }
        }
        return null;
    }

    private BaseMessage dealView(Map<String, String> requestMap) {
        return new TextMessage(requestMap, "小黑子！");
    }

    private BaseMessage dealClick(Map<String, String> requestMap) {
        String eventKey = requestMap.get("EventKey");
        switch (eventKey) {
            // 获取当日课表
            case "KUST_Course": {
                String coursesStr = courseService.getCoursesByOpenId(requestMap.get("FromUserName"));
                return new TextMessage(requestMap, coursesStr);
            }
            case "KUST_Weather": {
                String weather = weatherService.getWeather();
                return new TextMessage(requestMap, weather);
            }
            default: {
                return new TextMessage(requestMap, "该事件尚未定义！");
            }
        }
    }

    /**
     * 处理文本消息
     *
     * @param requestMap 请求参数
     * @return TextMessage
     */
    private BaseMessage dealTextMessage(Map<String, String> requestMap) {

        String content = requestMap.get("Content");

        if (content.contains("课表")) {
            System.out.println("请求课表!");
            String coursesByOpenId = courseService.getCoursesByOpenId(requestMap.get("FromUserName"));
            return new TextMessage(requestMap, coursesByOpenId);
        }

        if (content.contains("[绑定+")) {
            String regex = "(?<=\\+).*?(?=\\])";
            Pattern p = Pattern.compile(regex);
            Matcher m = p.matcher(content);
            String userName = "";
            if (m.find()) {
                userName = m.group();
                QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
                userQueryWrapper.eq("username", userName);
                User user = userMapper.selectOne(userQueryWrapper);
                if (user == null) {
                    return new TextMessage(requestMap, "用户:" + userName + "不存在!\n请联系管理员进行注册!");
                } else {
                    user.setOpenId(requestMap.get("FromUserName"));
                    userMapper.updateById(user);
                    return new TextMessage(requestMap, "绑定成功!\n欢迎来到昆工!");
                }
            } else {
                return new TextMessage(requestMap, "输入有误!");
            }
        }

        if (content.contains("天气")) {
            String weather = weatherService.getWeather();
            return new TextMessage(requestMap, weather);
        }

        return new TextMessage(requestMap, "你干嘛!");
    }

    private String ObjectToXML(BaseMessage msg) {
        XStream stream = new XStream();
        stream.processAnnotations(TextMessage.class);
        return stream.toXML(msg);
    }

    public List<String> getUserList() {
        String url = GetUserListURL.replace("ACCESS_TOKEN", getAccessToken()).replace("NEXT_OPENID", "");
        String s = utils.get(url);
        JSONObject jsonObject = JSONObject.parseObject(s);
        if (jsonObject != null) {
            return jsonObject.getJSONObject("data").getList("openid", String.class);
        }
        return null;
    }
}

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
     * 1??????token???timestamp???nonce?????????????????????????????????
     * 2?????????????????????????????????????????????????????????sha1??????
     * 3????????????????????????????????????????????? signature ???????????????????????????????????????
     *
     * @param map ??????????????????
     * @return ??????????????????
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
            // ??????????????????
            MessageDigest md = MessageDigest.getInstance("sha1");
            // ????????????
            byte[] digest = md.digest(str.getBytes());
            // ??????????????????
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
     * ??????XML?????????
     *
     * @param is ?????????
     * @return ?????????
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
     * ??????????????????????????????
     *
     * @param requestMap ????????????
     * @return XML?????????
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
                msg = new TextMessage(requestMap, "?????????!");
                break;
            }
        }
//        return stream.toXML(msg);
        return ObjectToXML(msg);
    }

    /**
     * ??????????????????
     *
     * @param requestMap ????????????
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
        return new TextMessage(requestMap, "????????????");
    }

    private BaseMessage dealClick(Map<String, String> requestMap) {
        String eventKey = requestMap.get("EventKey");
        switch (eventKey) {
            // ??????????????????
            case "KUST_Course": {
                String coursesStr = courseService.getCoursesByOpenId(requestMap.get("FromUserName"));
                return new TextMessage(requestMap, coursesStr);
            }
            case "KUST_Weather": {
                String weather = weatherService.getWeather();
                return new TextMessage(requestMap, weather);
            }
            case "KUST_Course_to": {
                String courseStr = courseService.getTomorrowCourse(requestMap.get("FromUserName"));
                return new TextMessage(requestMap, courseStr);
            }
            default: {
                return new TextMessage(requestMap, "????????????????????????");
            }
        }
    }

    /**
     * ??????????????????
     *
     * @param requestMap ????????????
     * @return TextMessage
     */
    private BaseMessage dealTextMessage(Map<String, String> requestMap) {

        String content = requestMap.get("Content");

        if (content.contains("??????")) {
            System.out.println("????????????!");
            String coursesByOpenId = courseService.getCoursesByOpenId(requestMap.get("FromUserName"));
            return new TextMessage(requestMap, coursesByOpenId);
        }

        if (content.contains("[??????+")) {
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
                    return new TextMessage(requestMap, "??????:" + userName + "?????????!\n??????????????????????????????!");
                } else {
                    user.setOpenId(requestMap.get("FromUserName"));
                    userMapper.updateById(user);
                    return new TextMessage(requestMap, "????????????!\n??????????????????!");
                }
            } else {
                return new TextMessage(requestMap, "????????????!");
            }
        }

        if (content.contains("??????")) {
            String weather = weatherService.getWeather();
            return new TextMessage(requestMap, weather);
        }

        if (content.contains("??????")) {
            String time = utils.getTime();
            return new TextMessage(requestMap, time);
        }

        return new TextMessage(requestMap, "?????????!");
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

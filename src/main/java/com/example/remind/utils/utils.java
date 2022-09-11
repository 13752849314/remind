package com.example.remind.utils;


import org.apache.commons.io.IOUtils;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;;

public class utils {
    public static String RemindStringHTML(String username, String text) {
        return "<html>\n" +
                "<body>\n" +
                "<h1>亲爱的" + username + ":</h1>\n" +
                "<h3 style='color:red'>" + text + "</h3>\n" +
                "</body>\n" +
                "</html>";
    }

    public static Integer dateGetWeeks(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        Integer day = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (day.equals(0)) {
            day = 7;
        }
        return day;
    }

    public static Date StringToDate(String time) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.parse(time);
    }

    public static String get(String url) {
        try {
            URL urlObj = new URL(url);
            // get connect
            URLConnection connection = urlObj.openConnection();
            InputStream is = connection.getInputStream();
            return IOUtils.toString(is, StandardCharsets.UTF_8);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String post(String url, String data) {
        try {
            URL urlObj = new URL(url);
            // get connect
            URLConnection connection = urlObj.openConnection();
            connection.setDoOutput(true);
            OutputStream os = connection.getOutputStream();
            os.write(data.getBytes(StandardCharsets.UTF_8));
            os.close();

            InputStream is = connection.getInputStream();

            return IOUtils.toString(is, StandardCharsets.UTF_8);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) throws ParseException {
        System.out.println(dateGetWeeks(new Date()));
        System.out.println(dateGetWeeks(StringToDate("2022-09-09")));
        System.out.println(dateGetWeeks(StringToDate("2022-09-10")));
        System.out.println(dateGetWeeks(StringToDate("2022-09-11")));
        System.out.println(dateGetWeeks(StringToDate("2022-09-12")));
        Date date = new Date();
        System.out.println(StringToDate("2022-09-01").compareTo(StringToDate("2022-09-01")));
        StringBuilder builder = new StringBuilder();
        builder.append("12345");
        System.out.println(builder);
        System.out.println(builder.length());

        System.out.println("123".contains("12"));

        Calendar instance = Calendar.getInstance();
        instance.setTime(new Date());
        System.out.println(instance.getTime());
        instance.add(Calendar.DATE, 30);
        System.out.println(instance.getTime());
    }

}

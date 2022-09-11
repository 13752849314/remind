package com.example.remind.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.sql.Date;

@Data
@TableName("course")
public class Course implements Comparable<Course> {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @TableField("username")
    private String username;

    @TableField("courseName")
    private String courseName;

    @TableField("location")
    private String location;

    @TableField("teacher")
    private String teacher;

    @TableField("startAt")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date startAt;

    @TableField("total")
    private Integer total;

    @TableField("time")
    private String time;


    @Override
    public int compareTo(Course o) {
        String time1 = o.getTime().split("-")[0].split(":")[0];
        String time2 = this.getTime().split("-")[0].split(":")[0];
        Integer integer1 = Integer.parseInt(time1);
        Integer integer2 = Integer.parseInt(time2);
        return integer2.compareTo(integer1);
    }

    public String getInfo() {
        return "课程名: " + courseName + "\n" +
                "时间: " + time + "\n" +
                "地点: " + location + "\n" +
                "老师: " + teacher + "\n";
    }
}

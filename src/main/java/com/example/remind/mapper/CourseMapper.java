package com.example.remind.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.remind.entity.Course;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseMapper extends BaseMapper<Course> {
    List<Course> getCourseByUsername(String username);
}

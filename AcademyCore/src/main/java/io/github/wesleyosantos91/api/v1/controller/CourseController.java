package io.github.wesleyosantos91.api.v1.controller;

import io.github.wesleyosantos91.domain.entity.CourseEntity;
import io.github.wesleyosantos91.domain.repository.CourseRepository;
import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/courses")
public class CourseController {

    private final CourseRepository courseRepository;

    public CourseController(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    @GetMapping
    public List<CourseEntity> findAll() {
        return courseRepository.findAll();
    }
}

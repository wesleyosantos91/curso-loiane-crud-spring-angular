package io.github.wesleyosantos91.api.v1.controller;

import static io.github.wesleyosantos91.core.mapper.CourseMapper.MAPPER;

import io.github.wesleyosantos91.api.v1.request.CourseRequest;
import io.github.wesleyosantos91.api.v1.response.CourseResponse;
import io.github.wesleyosantos91.domain.entity.CourseEntity;
import io.github.wesleyosantos91.domain.repository.CourseRepository;
import io.github.wesleyosantos91.domain.service.CourseService;
import java.util.List;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StopWatch;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/courses")
public class CourseController {

    private static final Logger LOGGER = LoggerFactory.getLogger(CourseController.class);
    private final CourseService service;

    private final CourseRepository courseRepository;


    public CourseController(CourseService service, CourseRepository courseRepository) {
        this.service = service;
        this.courseRepository = courseRepository;
    }

    @GetMapping
    public List<CourseEntity> findAll() {
        return courseRepository.findAll();
    }

    @PostMapping
    public ResponseEntity<CourseResponse> create(@RequestBody CourseRequest request) {
        final StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        LOGGER.debug("Function started 'create customer'");
        final var response = MAPPER.toResponse(service.create(MAPPER.toModel(request)));
        stopWatch.stop();
        LOGGER.debug("Finished function with sucess 'create customer {}' in {} ms", response, stopWatch.getTotalTimeMillis());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}

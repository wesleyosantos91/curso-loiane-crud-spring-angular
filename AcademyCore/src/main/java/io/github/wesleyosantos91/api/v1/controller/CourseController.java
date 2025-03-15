package io.github.wesleyosantos91.api.v1.controller;

import static io.github.wesleyosantos91.core.mapper.CourseMapper.MAPPER;

import io.github.wesleyosantos91.api.v1.request.CourseRequest;
import io.github.wesleyosantos91.api.v1.response.CourseResponse;
import io.github.wesleyosantos91.core.validation.Groups;
import io.github.wesleyosantos91.domain.entity.CourseEntity;
import io.github.wesleyosantos91.domain.exception.ResourceNotFoundException;
import io.github.wesleyosantos91.domain.repository.CourseRepository;
import io.github.wesleyosantos91.domain.service.CourseService;
import java.util.List;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StopWatch;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/courses")
public record CourseController(CourseService service, CourseRepository repository) {

    private static final Logger LOGGER = LoggerFactory.getLogger(CourseController.class);

    @GetMapping
    public List<CourseEntity> findAll() {
        return repository.findAll();
    }

    @PostMapping
    public ResponseEntity<CourseResponse> create(@RequestBody CourseRequest request) {
        final StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        LOGGER.debug("Function started 'create course'");
        final var response = MAPPER.toResponse(service.create(MAPPER.toModel(request)));
        stopWatch.stop();
        LOGGER.debug("Finished function with success 'create course {}' in {} ms", response, stopWatch.getTotalTimeMillis());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<CourseResponse> getById(@PathVariable UUID id) throws ResourceNotFoundException {
        final StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        LOGGER.debug("Function started 'getById course' with id {}", id);
        final var course = service.findById(id);
        final var response = MAPPER.toResponse(course);
        stopWatch.stop();
        LOGGER.debug("Finished function with success 'getById course' {} in {} ms", response, stopWatch.getTotalTimeMillis());
        return ResponseEntity.ok().body(response);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<CourseResponse> update(@PathVariable UUID id,
                                                 @Validated(Groups.Update.class)
                                                 @RequestBody CourseRequest request) throws ResourceNotFoundException {
        final StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        LOGGER.debug("Function started 'update course'");
        final var course = service.update(id, MAPPER.toModel(request));
        stopWatch.stop();
        LOGGER.debug("Finished function with success 'update course' {} in {} ms", course, stopWatch.getTotalTimeMillis());
        return ResponseEntity.status(HttpStatus.OK).body(MAPPER.toResponse(course));
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) throws ResourceNotFoundException {
        final StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        LOGGER.debug("Function started 'delete course' with id {}", id);
        service.delete(id);
        stopWatch.stop();
        LOGGER.debug("Finished function with success 'delete person' in {} ms", stopWatch.getTotalTimeMillis());

        return ResponseEntity.noContent().build();
    }
}

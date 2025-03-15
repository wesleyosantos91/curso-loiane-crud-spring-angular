package io.github.wesleyosantos91.domain.service;

import static io.github.wesleyosantos91.core.mapper.CourseMapper.MAPPER;
import static java.text.MessageFormat.format;

import io.github.wesleyosantos91.domain.entity.CourseEntity;
import io.github.wesleyosantos91.domain.exception.ResourceNotFoundException;
import io.github.wesleyosantos91.domain.model.CourseModel;
import io.github.wesleyosantos91.domain.repository.CourseRepository;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CourseService {

    private final CourseRepository repository;

    public CourseService(CourseRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public CourseModel create(CourseModel model) {
        final var entity = repository.save(MAPPER.toEntity(model));
        return MAPPER.toModel(entity);
    }

    @Transactional(readOnly = true)
    public CourseModel findById(UUID id) throws ResourceNotFoundException {
        return repository.findById(id)
                .map(MAPPER::toModel)
                .orElseThrow(() -> new ResourceNotFoundException(format("Not found regitstry with code {0}", id)));
    }

    @Transactional
    public CourseModel update(UUID id, CourseModel model) throws ResourceNotFoundException {
        final var course = MAPPER.toEntity(model, getCourse(id));
        final var courseUpdated = repository.save(course);
        return MAPPER.toModel(courseUpdated);
    }

    @Transactional
    public void delete(UUID id) throws ResourceNotFoundException {

        final var course = getCourse(id);
        repository.delete(course);
    }

    private CourseEntity getCourse(UUID id) throws ResourceNotFoundException {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(format("Not found registry with code {0}", id)));
    }
}

package io.github.wesleyosantos91.domain.service;

import static io.github.wesleyosantos91.core.mapper.CourseMapper.MAPPER;

import io.github.wesleyosantos91.domain.model.CourseModel;
import io.github.wesleyosantos91.domain.repository.CourseRepository;
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
}

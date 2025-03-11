package io.github.wesleyosantos91.domain.repository;

import io.github.wesleyosantos91.domain.entity.CourseEntity;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
public interface CourseRepository extends JpaRepository<CourseEntity, UUID> {
}

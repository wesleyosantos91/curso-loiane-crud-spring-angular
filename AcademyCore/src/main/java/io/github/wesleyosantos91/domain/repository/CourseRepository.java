package io.github.wesleyosantos91.domain.repository;

import io.github.wesleyosantos91.domain.entity.CourseEntity;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

@Service
public interface CourseRepository extends JpaRepository<CourseEntity, UUID> {


    @Query("SELECT c FROM CourseEntity c WHERE "
            + "(:#{#query.id} IS NULL OR c.id = :#{#query.id}) "
            + "AND (:#{#query.name} IS NULL OR LOWER(c.name) LIKE LOWER(CONCAT('%', :#{#query.name}, '%'))) "
            + "AND (:#{#query.category} IS NULL OR LOWER(c.category) = LOWER(:#{#query.category}))"
            + "AND c.status = 'Active'")
    Page<CourseEntity> search(@Param("query") CourseEntity query, Pageable pageable);
}

package io.github.wesleyosantos91.domain.entity;

import io.github.wesleyosantos91.domain.entity.converter.CategoryEnumConverter;
import io.github.wesleyosantos91.domain.entity.converter.CourseStatusEnumConverter;
import io.github.wesleyosantos91.domain.entity.enums.CategoryEnum;
import io.github.wesleyosantos91.domain.entity.enums.CourseStatusEnum;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Table(name = "tb_courses")
@SQLDelete(sql = "UPDATE tb_courses SET status = 'Inactive' WHERE id = ?")
@SQLRestriction("status = 'Active'")
public class CourseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false)
    private UUID id;

    @Size(min = 5, max = 100)
    @Column(name = "name", length = 100, nullable = false)
    private String name;

    @Size(min = 5, max = 10)
    @Convert(converter = CategoryEnumConverter.class)
    @Column(name = "category", length = 10, nullable = false)
    private CategoryEnum category;

    @Size(min = 5, max = 10)
    @Convert(converter = CourseStatusEnumConverter.class)
    @Column(name = "status", length = 10, nullable = false)
    private CourseStatusEnum status = CourseStatusEnum.ACTIVE;

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<LessonEntity> lessons = new LinkedHashSet<>();

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public CategoryEnum getCategory() {
        return category;
    }

    public void setCategory(CategoryEnum category) {
        this.category = category;
    }

    public Set<LessonEntity> getLessons() {
        return lessons;
    }

    public void setLessons(Set<LessonEntity> lessonEntities) {
        this.lessons = lessonEntities;
    }

}

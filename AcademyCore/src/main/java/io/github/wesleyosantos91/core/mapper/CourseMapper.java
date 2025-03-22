package io.github.wesleyosantos91.core.mapper;

import static org.mapstruct.NullValueCheckStrategy.ALWAYS;
import static org.mapstruct.NullValuePropertyMappingStrategy.IGNORE;

import io.github.wesleyosantos91.api.v1.request.CourseQueryRequest;
import io.github.wesleyosantos91.api.v1.request.CourseRequest;
import io.github.wesleyosantos91.api.v1.request.LessonRequest;
import io.github.wesleyosantos91.api.v1.response.CourseResponse;
import io.github.wesleyosantos91.domain.entity.CourseEntity;
import io.github.wesleyosantos91.domain.entity.LessonEntity;
import io.github.wesleyosantos91.domain.entity.enums.CategoryEnum;
import io.github.wesleyosantos91.domain.model.CourseModel;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Component;

@Mapper(nullValuePropertyMappingStrategy = IGNORE,
        nullValueCheckStrategy = ALWAYS,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
@Component
public interface CourseMapper {

    LessonMapper LESSON_MAPPER = Mappers.getMapper(LessonMapper.class);
    CourseMapper MAPPER = Mappers.getMapper(CourseMapper.class);

    @Mappings({
            @Mapping(target = "lessons", expression = "java(LESSON_MAPPER.requestToSetModel(request.lessons()))")
    })
    CourseModel toModel(CourseRequest request);

    CourseModel toModel(CourseQueryRequest query);

    @Mappings({
            @Mapping(source = "category", target = "category", qualifiedByName = "toCategoryString"),
            @Mapping(target = "lessons", expression = "java(LESSON_MAPPER.toSetModel(entity.getLessons()))")
    })
    CourseModel toModel(CourseEntity entity);

    @Mappings({
            @Mapping(source = "category", target = "category", qualifiedByName = "toCategoryEnum"),
            @Mapping(target = "lessons", expression = "java(LESSON_MAPPER.toSetEntity(model.getLessons()))")
    })
    CourseEntity toEntity(CourseModel model);

    @Mappings({
            @Mapping(source = "category", target = "category", qualifiedByName = "toCategoryEnum"),
            @Mapping(target = "lessons", ignore = true)
    })
    CourseEntity toEntity(CourseModel model, @MappingTarget CourseEntity entity);

    @Mappings({
            @Mapping(target = "lessons", expression = "java(LESSON_MAPPER.toSetResponse(model.getLessons()))")
    })
    CourseResponse toResponse(CourseModel model);

    default List<CourseModel> toModelList(List<CourseEntity> entities) {
        return entities.stream().map(this::toModel).collect(Collectors.toList());
    }

    default List<CourseResponse> toResponseList(List<CourseModel> models) {
        return models.stream().map(this::toResponse).collect(Collectors.toList());
    }

    default Page<CourseModel> toPageModel(Page<CourseEntity> pages) {
        final List<CourseModel> models = toModelList(pages.getContent());
        return new PageImpl<>(models, pages.getPageable(), pages.getTotalElements());
    }

    default Page<CourseResponse> toPageResponse(Page<CourseModel> pages) {
        final List<CourseResponse> models = toResponseList(pages.getContent());
        return new PageImpl<>(models, pages.getPageable(), pages.getTotalElements());
    }

    @Named("toCategoryEnum")
    default CategoryEnum toCategoryEnum(String category) {
        return CategoryEnum.fromValue(category);
    }

    @Named("toCategoryString")
    default String toCategoryString(CategoryEnum category) {
        return category.getValue();
    }

    @AfterMapping
    default void updateLessons(CourseModel model, @MappingTarget CourseEntity entity) {
        Optional.ofNullable(model.getLessons())
                .map(LESSON_MAPPER::toSetEntity)
                .ifPresent(lessonEntities -> {
                    lessonEntities.forEach(lesson -> lesson.setCourse(entity));
                    entity.getLessons().clear();
                    entity.getLessons().addAll(lessonEntities);
                });
    }

}

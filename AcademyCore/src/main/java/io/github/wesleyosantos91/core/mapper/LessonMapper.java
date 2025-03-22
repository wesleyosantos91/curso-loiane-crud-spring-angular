package io.github.wesleyosantos91.core.mapper;

import static org.mapstruct.NullValueCheckStrategy.ALWAYS;
import static org.mapstruct.NullValuePropertyMappingStrategy.IGNORE;

import io.github.wesleyosantos91.api.v1.request.LessonRequest;
import io.github.wesleyosantos91.api.v1.response.LessonResponse;
import io.github.wesleyosantos91.domain.entity.LessonEntity;
import io.github.wesleyosantos91.domain.model.LessonModel;
import java.util.Set;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.ReportingPolicy;
import org.springframework.stereotype.Component;

@Mapper(nullValuePropertyMappingStrategy = IGNORE,
        nullValueCheckStrategy = ALWAYS,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
@Component
public interface LessonMapper {

    @Mappings({
            @Mapping(target = "course", ignore = true)
    })
    LessonModel toModel(LessonEntity entity);

    @Mappings({
            @Mapping(target = "course", ignore = true)
    })
    LessonModel toModel(LessonRequest request);

    @Mappings({
            @Mapping(target = "course", ignore = true)
    })
    LessonEntity toEntity(LessonModel model);

    @Mappings({
            @Mapping(target = "course", ignore = true)
    })
    LessonResponse toResponse(LessonModel model);

    Set<LessonModel> toSetModel(Set<LessonEntity> entities);

    Set<LessonModel> requestToSetModel(Set<LessonRequest> requests);

    Set<LessonEntity> toSetEntity(Set<LessonModel> models);

    Set<LessonResponse> toSetResponse(Set<LessonModel> models);
}

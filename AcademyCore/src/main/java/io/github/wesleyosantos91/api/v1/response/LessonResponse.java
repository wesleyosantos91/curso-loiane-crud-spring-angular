package io.github.wesleyosantos91.api.v1.response;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import java.util.UUID;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record LessonResponse(
        UUID id,
        String name,
        String youtubeUrl,
        @JsonBackReference
        CourseResponse course
) {}

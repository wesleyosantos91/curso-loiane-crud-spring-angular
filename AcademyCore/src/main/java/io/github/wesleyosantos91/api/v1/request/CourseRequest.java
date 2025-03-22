package io.github.wesleyosantos91.api.v1.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.github.wesleyosantos91.core.validation.Groups;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.util.Set;


@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record CourseRequest(
        @NotBlank(groups = {Groups.Create.class, Groups.Update.class})
        @Size(min = 5, max = 100, groups = {Groups.Create.class, Groups.Update.class})
        String name,
        @NotBlank(groups = {Groups.Create.class, Groups.Update.class})
        @Size(max = 10, groups = {Groups.Create.class, Groups.Update.class})
        @Pattern(regexp = "Back-end|Front-end", groups = {Groups.Create.class, Groups.Update.class})
        String category,
        Set<LessonRequest> lessons
) {
}

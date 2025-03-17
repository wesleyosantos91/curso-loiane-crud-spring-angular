package io.github.wesleyosantos91.domain.entity.converter;


import io.github.wesleyosantos91.domain.entity.enums.CourseStatusEnum;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class CourseStatusEnumConverter implements AttributeConverter<CourseStatusEnum, String> {

    @Override
    public String convertToDatabaseColumn(CourseStatusEnum status) {
        return status != null ? status.getValue() : null;
    }

    @Override
    public CourseStatusEnum convertToEntityAttribute(String dbData) {
        return dbData != null ? CourseStatusEnum.fromValue(dbData) : null;
    }
}

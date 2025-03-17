package io.github.wesleyosantos91.domain.entity.converter;


import io.github.wesleyosantos91.domain.entity.enums.CategoryEnum;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class CategoryEnumConverter implements AttributeConverter<CategoryEnum, String> {

    @Override
    public String convertToDatabaseColumn(CategoryEnum category) {
        return category != null ? category.getValue() : null;
    }

    @Override
    public CategoryEnum convertToEntityAttribute(String dbData) {
        return dbData != null ? CategoryEnum.fromValue(dbData) : null;
    }
}

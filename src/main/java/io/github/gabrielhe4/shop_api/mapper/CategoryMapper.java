package io.github.gabrielhe4.shop_api.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

import io.github.gabrielhe4.shop_api.dto.CategoryRequestDTO;
import io.github.gabrielhe4.shop_api.model.Category;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

    CategoryMapper INSTANCE = Mappers.getMapper(CategoryMapper.class);

    @Mapping(target = "id", ignore = true)
    Category toNewEntity(CategoryRequestDTO request);

    void updateEntity(
        CategoryRequestDTO request,
        @MappingTarget Category category
    );
}

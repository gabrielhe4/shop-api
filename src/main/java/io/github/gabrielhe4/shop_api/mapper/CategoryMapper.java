package io.github.gabrielhe4.shop_api.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

import io.github.gabrielhe4.shop_api.dto.CategoryDTO;
import io.github.gabrielhe4.shop_api.dto.CategoryRequest;
import io.github.gabrielhe4.shop_api.model.Category;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

    CategoryMapper INSTANCE = Mappers.getMapper(CategoryMapper.class);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "products", ignore = true)
    Category toNewEntity(CategoryRequest request);

    void updateEntity(
        CategoryRequest request,
        @MappingTarget Category category
    );

    CategoryDTO toDTO(Category category);
}

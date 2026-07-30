package io.github.gabrielhe4.shop_api.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import io.github.gabrielhe4.shop_api.dto.ProductDTO;
import io.github.gabrielhe4.shop_api.dto.ProductRequest;
import io.github.gabrielhe4.shop_api.model.Product;

@Mapper
public interface ProductMapper {

    ProductMapper INSTANCE = Mappers.getMapper(ProductMapper.class);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "category", ignore = true)
    @Mapping(target = "image", ignore = true)
    @Mapping(target = "specialPrice", ignore = true)
    Product toEntity(ProductRequest request);

    ProductDTO toDTO(Product product);
}

package io.github.gabrielhe4.shop_api.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import io.github.gabrielhe4.shop_api.dto.OrderItemDTO;
import io.github.gabrielhe4.shop_api.model.OrderItem;

@Mapper
public interface OrderItemMapper {

    OrderItemMapper INSTANCE = Mappers.getMapper(OrderItemMapper.class);

    @Mapping(source = "id", target = "orderItemId")
    @Mapping(target = "product", ignore = true)
    OrderItemDTO toDTO(OrderItem entity);

}

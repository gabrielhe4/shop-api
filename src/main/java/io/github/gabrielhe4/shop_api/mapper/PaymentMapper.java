package io.github.gabrielhe4.shop_api.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import io.github.gabrielhe4.shop_api.dto.PaymentDTO;
import io.github.gabrielhe4.shop_api.model.Payment;

@Mapper
public interface PaymentMapper {

    PaymentMapper INSTANCE = Mappers.getMapper(PaymentMapper.class);

    @Mapping(source = "id", target = "paymentId")
    PaymentDTO toDTO(Payment entity);

}

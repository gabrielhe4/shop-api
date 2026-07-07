package io.github.gabrielhe4.shop_api.dto;

public record OrderRequestDTO(
    Long addressId,
    Long paymentMethod,
    String pgName,
    String pgPaymentId,
    String pgStatus,
    String pgResponseMessage
) {

}

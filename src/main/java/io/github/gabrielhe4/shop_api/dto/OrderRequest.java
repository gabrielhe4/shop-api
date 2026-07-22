package io.github.gabrielhe4.shop_api.dto;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * DTO for order creation request.
 */
@Schema(
    name = "OrderRequest",
    description = "Represents an order request with address, payment method, and gateway information.",
    implementation = OrderRequest.class,
    example = """
        {
            "addressId": 123,
            "paymentMethod": "CARD",
            "pgName": "stripe",
            "pgPaymentId": "pi_3Kzfy6ff4s",
            "pgStatus": "success",
            "pgResponseMessage": "Payment completed successfully"
        }
        """
)
public record OrderRequest(
    @Schema(description = "Address ID", example = "123")
    Long addressId,
    @Schema(description = "Payment Method", example = "CARD")
    String paymentMethod,
    @Schema(description = "Payment Gateway Name", example = "stripe")
    String pgName,
    @Schema(description = "Payment Gateway Transaction ID", example = "pi_3Kz...")
    String pgPaymentId,
    @Schema(description = "Payment Gateway Status", example = "success|pending|failed")
    String pgStatus,
    @Schema(description = "Payment Gateway Response Message", example = "Payment completed successfully")
    String pgResponseMessage
) {

}


package io.github.gabrielhe4.shop_api.dto;

import java.util.List;

public record UserInfoResponse(
    Long id,
    String username,
    List<String> roles
) {

}

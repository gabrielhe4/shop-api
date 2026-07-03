package io.github.gabrielhe4.shop_api.dto;

import java.util.List;

public record UserInfoDTO(
    Long id,
    String username,
    List<String> roles,
    String cookie
) {
}

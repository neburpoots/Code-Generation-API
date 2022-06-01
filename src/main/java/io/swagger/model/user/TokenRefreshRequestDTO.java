package io.swagger.model.user;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class TokenRefreshRequestDTO {
    @NotNull
    private String refreshToken;
    public String getRefreshToken() {
        return refreshToken;
    }
    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}

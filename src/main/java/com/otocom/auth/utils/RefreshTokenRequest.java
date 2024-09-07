package com.otocom.auth.utils;

import lombok.Data;

@Data
public class RefreshTokenRequest {

    private String refreshToken;
}
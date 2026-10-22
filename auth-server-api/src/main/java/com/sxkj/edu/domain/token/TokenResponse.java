package com.sxkj.edu.domain.token;

import lombok.Data;

@Data
public class TokenResponse {

    private Integer expires_in;

    private String access_token;

    private String token_type;

    private String refresh_token;

    private String scope;

    private String license;
}

package com.sxkj.edu.domain.token;

import lombok.Data;

@Data
public class TokenApiResponse {

    private Integer code;

    private String message;

    private String description;

    private String uuid;

    private TokenResponse result;
}

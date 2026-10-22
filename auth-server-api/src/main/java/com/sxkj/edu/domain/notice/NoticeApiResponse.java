package com.sxkj.edu.domain.notice;

import lombok.Data;

@Data
public class NoticeApiResponse {

    private Integer code;

    private String message;

    private String description;

    private String uuid;

    private NoticePageResponse result;
}

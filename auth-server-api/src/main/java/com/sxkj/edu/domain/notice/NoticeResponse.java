package com.sxkj.edu.domain.notice;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class NoticeResponse {

    @JsonProperty("ID")
    private String linkId;

    @JsonProperty("TITLE")
    private String title;

    @JsonProperty("URL")
    private String linkUrl;

    @JsonProperty("FBSJ")
    private String time;

}

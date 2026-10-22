package com.sxkj.edu.domain.notice;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class NoticePageResponse {

    private Integer page;

    private Integer per;

    private Integer total;

    private Integer max_page;

    private Map<String, String> data_struct;

    private List<NoticeResponse> data;
}

package com.sxkj.edu.domain.sys.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Setter
@Getter
@ToString
public class SearchPersonDTO {
    private List<String> groupIds;
    private String image;
    private String url;
}

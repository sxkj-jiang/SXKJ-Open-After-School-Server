package com.sxkj.edu.common.base.domain;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;



@Slf4j
@Data
public class OrderItemDTO {
    private String column;
    private boolean asc = true;
}

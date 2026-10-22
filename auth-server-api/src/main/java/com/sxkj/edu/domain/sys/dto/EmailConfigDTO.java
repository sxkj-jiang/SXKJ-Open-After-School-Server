package com.sxkj.edu.domain.sys.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmailConfigDTO {

    private String smtpHost;

    private String username;

    private String password;

}

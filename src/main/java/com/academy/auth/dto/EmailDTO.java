package com.academy.auth.dto;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.util.List;
import java.util.Map;

@Data
@ToString
@Builder
public class EmailDTO {

    private String to;
    private String subject;
    private Map<String, Object> templateVariables;
    private String applicationName;
    private String templateId;

}

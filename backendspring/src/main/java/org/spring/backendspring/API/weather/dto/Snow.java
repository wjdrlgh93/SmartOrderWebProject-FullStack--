package org.spring.backendspring.API.weather.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Snow {
    /** 지난 1 시간 동안의 강설량, mm */
    @JsonProperty("1h")
    private float snow1h;
}

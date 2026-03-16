package com.airquality.dto;

import lombok.Data;

@Data
public class AirQualityQueryDTO {
    private Long cityId;
    private String cityName;
    private String qualityLevel;
    private String startTime;
    private String endTime;
    private Integer pageNum = 1;
    private Integer pageSize = 10;
}

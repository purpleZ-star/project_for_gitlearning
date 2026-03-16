package com.airquality.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AirQualityData {
    private Long id;
    private Long cityId;
    private Integer aqi;
    private String qualityLevel;
    private String primaryPollutant;
    private Double pm25;
    private Double pm10;
    private Double so2;
    private Double no2;
    private Double co;
    private Double o3;
    private Double temperature;
    private Double humidity;
    private Double windSpeed;
    private String windDirection;
    private LocalDateTime monitorTime;
    private LocalDateTime createTime;

    /** 关联城市名称（非数据库字段） */
    private String cityName;
    private String province;
}

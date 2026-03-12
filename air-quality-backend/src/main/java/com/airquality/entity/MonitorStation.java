package com.airquality.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MonitorStation {
    private Long id;
    private String stationName;
    private String stationCode;
    private Long cityId;
    private String address;
    private Double longitude;
    private Double latitude;
    private Integer status;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;

    /** 关联字段 */
    private String cityName;
}

package com.airquality.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class City {
    private Long id;
    private String cityName;
    private String province;
    private Double longitude;
    private Double latitude;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}

package com.airquality.service;

import com.airquality.common.PageResult;
import com.airquality.dto.AirQualityQueryDTO;
import com.airquality.dto.StatisticsDTO;
import com.airquality.entity.AirQualityData;

import java.util.List;
import java.util.Map;

public interface AirQualityDataService {

    PageResult<AirQualityData> findByCondition(AirQualityQueryDTO query);

    AirQualityData findById(Long id);

    void save(AirQualityData data);

    void update(AirQualityData data);

    void deleteById(Long id);

    List<AirQualityData> findLatestByCity();

    List<AirQualityData> findTrendData(Long cityId, int days);

    List<StatisticsDTO> countByQualityLevel();

    List<Map<String, Object>> rankByAvgAqi(int limit);

    Map<String, Object> getDashboardStats();

    Map<String, Object> avgPollutants();
}

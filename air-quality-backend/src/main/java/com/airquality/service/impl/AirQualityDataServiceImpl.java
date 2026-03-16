package com.airquality.service.impl;

import com.airquality.common.PageResult;
import com.airquality.dto.AirQualityQueryDTO;
import com.airquality.dto.StatisticsDTO;
import com.airquality.entity.AirQualityData;
import com.airquality.mapper.AirQualityDataMapper;
import com.airquality.mapper.CityMapper;
import com.airquality.mapper.MonitorStationMapper;
import com.airquality.service.AirQualityDataService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AirQualityDataServiceImpl implements AirQualityDataService {

    @Resource
    private AirQualityDataMapper airQualityDataMapper;

    @Resource
    private CityMapper cityMapper;

    @Resource
    private MonitorStationMapper monitorStationMapper;

    @Override
    public PageResult<AirQualityData> findByCondition(AirQualityQueryDTO query) {
        PageHelper.startPage(query.getPageNum(), query.getPageSize());
        List<AirQualityData> list = airQualityDataMapper.findByCondition(query);
        PageInfo<AirQualityData> pageInfo = new PageInfo<>(list);
        return PageResult.of(pageInfo.getTotal(), pageInfo.getList());
    }

    @Override
    public AirQualityData findById(Long id) {
        return airQualityDataMapper.findById(id);
    }

    @Override
    public void save(AirQualityData data) {
        airQualityDataMapper.insert(data);
    }

    @Override
    public void update(AirQualityData data) {
        airQualityDataMapper.update(data);
    }

    @Override
    public void deleteById(Long id) {
        airQualityDataMapper.deleteById(id);
    }

    @Override
    public List<AirQualityData> findLatestByCity() {
        return airQualityDataMapper.findLatestByCity();
    }

    @Override
    public List<AirQualityData> findTrendData(Long cityId, int days) {
        return airQualityDataMapper.findTrendData(cityId, days);
    }

    @Override
    public List<StatisticsDTO> countByQualityLevel() {
        return airQualityDataMapper.countByQualityLevel();
    }

    @Override
    public List<Map<String, Object>> rankByAvgAqi(int limit) {
        return airQualityDataMapper.rankByAvgAqi(limit);
    }

    @Override
    public Map<String, Object> getDashboardStats() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("cityCount", cityMapper.count());
        stats.put("stationCount", monitorStationMapper.count());
        stats.put("dataCount", airQualityDataMapper.count());
        stats.put("todayCount", airQualityDataMapper.countToday());
        return stats;
    }

    @Override
    public Map<String, Object> avgPollutants() {
        return airQualityDataMapper.avgPollutants();
    }
}

package com.airquality.controller;

import com.airquality.common.PageResult;
import com.airquality.common.Result;
import com.airquality.dto.AirQualityQueryDTO;
import com.airquality.dto.StatisticsDTO;
import com.airquality.entity.AirQualityData;
import com.airquality.service.AirQualityDataService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/air-quality")
public class AirQualityDataController {

    @Resource
    private AirQualityDataService airQualityDataService;

    @GetMapping("/list")
    public Result<PageResult<AirQualityData>> list(AirQualityQueryDTO query) {
        return Result.success(airQualityDataService.findByCondition(query));
    }

    @GetMapping("/{id}")
    public Result<AirQualityData> getById(@PathVariable Long id) {
        return Result.success(airQualityDataService.findById(id));
    }

    @PostMapping
    public Result<?> save(@RequestBody AirQualityData data) {
        airQualityDataService.save(data);
        return Result.success();
    }

    @PutMapping
    public Result<?> update(@RequestBody AirQualityData data) {
        airQualityDataService.update(data);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result<?> delete(@PathVariable Long id) {
        airQualityDataService.deleteById(id);
        return Result.success();
    }

    /** 各城市最新空气质量数据 */
    @GetMapping("/latest")
    public Result<List<AirQualityData>> latestByCity() {
        return Result.success(airQualityDataService.findLatestByCity());
    }

    /** 城市空气质量趋势 */
    @GetMapping("/trend")
    public Result<List<AirQualityData>> trend(@RequestParam Long cityId,
                                               @RequestParam(defaultValue = "7") int days) {
        return Result.success(airQualityDataService.findTrendData(cityId, days));
    }

    /** 空气质量等级分布 */
    @GetMapping("/level-distribution")
    public Result<List<StatisticsDTO>> levelDistribution() {
        return Result.success(airQualityDataService.countByQualityLevel());
    }

    /** 城市AQI排名 */
    @GetMapping("/rank")
    public Result<List<Map<String, Object>>> rank(@RequestParam(defaultValue = "10") int limit) {
        return Result.success(airQualityDataService.rankByAvgAqi(limit));
    }

    /** 仪表盘统计数据 */
    @GetMapping("/dashboard")
    public Result<Map<String, Object>> dashboard() {
        return Result.success(airQualityDataService.getDashboardStats());
    }

    /** 各污染物平均值 */
    @GetMapping("/avg-pollutants")
    public Result<Map<String, Object>> avgPollutants() {
        return Result.success(airQualityDataService.avgPollutants());
    }
}

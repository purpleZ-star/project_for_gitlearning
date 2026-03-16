package com.airquality.mapper;

import com.airquality.dto.AirQualityQueryDTO;
import com.airquality.dto.StatisticsDTO;
import com.airquality.entity.AirQualityData;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface AirQualityDataMapper {

    List<AirQualityData> findByCondition(AirQualityQueryDTO query);

    AirQualityData findById(@Param("id") Long id);

    int insert(AirQualityData data);

    int update(AirQualityData data);

    int deleteById(@Param("id") Long id);

    /** 获取各城市最新AQI数据 */
    List<AirQualityData> findLatestByCity();

    /** 获取指定城市的历史趋势数据 */
    List<AirQualityData> findTrendData(@Param("cityId") Long cityId, @Param("days") int days);

    /** 空气质量等级分布统计 */
    List<StatisticsDTO> countByQualityLevel();

    /** 各城市平均AQI排名 */
    List<Map<String, Object>> rankByAvgAqi(@Param("limit") int limit);

    /** 统计总记录数 */
    int count();

    /** 今日数据量 */
    int countToday();

    /** 各污染物平均值统计 */
    Map<String, Object> avgPollutants();

    /** 批量插入 */
    int batchInsert(@Param("list") List<AirQualityData> list);
}

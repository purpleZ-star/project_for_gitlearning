package com.airquality.mapper;

import com.airquality.entity.MonitorStation;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MonitorStationMapper {

    List<MonitorStation> findAll();

    List<MonitorStation> findByKeyword(@Param("keyword") String keyword);

    MonitorStation findById(@Param("id") Long id);

    int insert(MonitorStation station);

    int update(MonitorStation station);

    int deleteById(@Param("id") Long id);

    int count();
}

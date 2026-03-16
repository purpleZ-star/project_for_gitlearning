package com.airquality.service;

import com.airquality.entity.MonitorStation;

import java.util.List;

public interface MonitorStationService {

    List<MonitorStation> findAll();

    List<MonitorStation> findByKeyword(String keyword);

    MonitorStation findById(Long id);

    void save(MonitorStation station);

    void update(MonitorStation station);

    void deleteById(Long id);

    int count();
}

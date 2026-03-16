package com.airquality.service.impl;

import com.airquality.entity.MonitorStation;
import com.airquality.mapper.MonitorStationMapper;
import com.airquality.service.MonitorStationService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class MonitorStationServiceImpl implements MonitorStationService {

    @Resource
    private MonitorStationMapper monitorStationMapper;

    @Override
    public List<MonitorStation> findAll() {
        return monitorStationMapper.findAll();
    }

    @Override
    public List<MonitorStation> findByKeyword(String keyword) {
        return monitorStationMapper.findByKeyword(keyword);
    }

    @Override
    public MonitorStation findById(Long id) {
        return monitorStationMapper.findById(id);
    }

    @Override
    public void save(MonitorStation station) {
        monitorStationMapper.insert(station);
    }

    @Override
    public void update(MonitorStation station) {
        monitorStationMapper.update(station);
    }

    @Override
    public void deleteById(Long id) {
        monitorStationMapper.deleteById(id);
    }

    @Override
    public int count() {
        return monitorStationMapper.count();
    }
}

package com.airquality.service.impl;

import com.airquality.entity.City;
import com.airquality.mapper.CityMapper;
import com.airquality.service.CityService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class CityServiceImpl implements CityService {

    @Resource
    private CityMapper cityMapper;

    @Override
    public List<City> findAll() {
        return cityMapper.findAll();
    }

    @Override
    public List<City> findByKeyword(String keyword) {
        return cityMapper.findByKeyword(keyword);
    }

    @Override
    public City findById(Long id) {
        return cityMapper.findById(id);
    }

    @Override
    public void save(City city) {
        cityMapper.insert(city);
    }

    @Override
    public void update(City city) {
        cityMapper.update(city);
    }

    @Override
    public void deleteById(Long id) {
        cityMapper.deleteById(id);
    }

    @Override
    public int count() {
        return cityMapper.count();
    }
}

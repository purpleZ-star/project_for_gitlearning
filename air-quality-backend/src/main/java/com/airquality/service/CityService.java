package com.airquality.service;

import com.airquality.entity.City;

import java.util.List;

public interface CityService {

    List<City> findAll();

    List<City> findByKeyword(String keyword);

    City findById(Long id);

    void save(City city);

    void update(City city);

    void deleteById(Long id);

    int count();
}

package com.airquality.mapper;

import com.airquality.entity.City;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CityMapper {

    List<City> findAll();

    List<City> findByKeyword(@Param("keyword") String keyword);

    City findById(@Param("id") Long id);

    int insert(City city);

    int update(City city);

    int deleteById(@Param("id") Long id);

    int count();
}

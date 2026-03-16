package com.airquality.controller;

import com.airquality.common.Result;
import com.airquality.entity.City;
import com.airquality.service.CityService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/api/city")
public class CityController {

    @Resource
    private CityService cityService;

    @GetMapping("/list")
    public Result<List<City>> list(@RequestParam(required = false) String keyword) {
        if (keyword != null && !keyword.isEmpty()) {
            return Result.success(cityService.findByKeyword(keyword));
        }
        return Result.success(cityService.findAll());
    }

    @GetMapping("/{id}")
    public Result<City> getById(@PathVariable Long id) {
        return Result.success(cityService.findById(id));
    }

    @PostMapping
    public Result<?> save(@RequestBody City city) {
        cityService.save(city);
        return Result.success();
    }

    @PutMapping
    public Result<?> update(@RequestBody City city) {
        cityService.update(city);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result<?> delete(@PathVariable Long id) {
        cityService.deleteById(id);
        return Result.success();
    }
}

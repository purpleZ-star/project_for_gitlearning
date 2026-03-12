package com.airquality.controller;

import com.airquality.common.Result;
import com.airquality.entity.MonitorStation;
import com.airquality.service.MonitorStationService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/api/station")
public class MonitorStationController {

    @Resource
    private MonitorStationService monitorStationService;

    @GetMapping("/list")
    public Result<List<MonitorStation>> list(@RequestParam(required = false) String keyword) {
        if (keyword != null && !keyword.isEmpty()) {
            return Result.success(monitorStationService.findByKeyword(keyword));
        }
        return Result.success(monitorStationService.findAll());
    }

    @GetMapping("/{id}")
    public Result<MonitorStation> getById(@PathVariable Long id) {
        return Result.success(monitorStationService.findById(id));
    }

    @PostMapping
    public Result<?> save(@RequestBody MonitorStation station) {
        monitorStationService.save(station);
        return Result.success();
    }

    @PutMapping
    public Result<?> update(@RequestBody MonitorStation station) {
        monitorStationService.update(station);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result<?> delete(@PathVariable Long id) {
        monitorStationService.deleteById(id);
        return Result.success();
    }
}

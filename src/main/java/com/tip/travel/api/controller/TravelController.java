package com.tip.travel.api.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.tip.travel.api.framework.annotation.Log;
import com.tip.travel.api.framework.constants.LogTypeConstant;
import com.tip.travel.common.domain.Travel;
import com.tip.travel.common.service.TravelService;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController("/public/api")
public class TravelController {
    @Reference
    private TravelService travelService;

    @GetMapping("/travel/{id}")
    @Log(logType = LogTypeConstant.LOGIN_TYPE)
    public Travel getTravelById(@PathVariable("id") Long id) {
        return travelService.getTravelById(id);
    }

    @PostMapping("/travel")
    public Integer createNewTravel(@RequestBody Travel travel) {
        return travelService.saveTravel(travel);
    }

    @DeleteMapping("/travel/{id}")
    public Integer deleteTravel(@PathVariable("id") Long id) {
        return travelService.deleteTravel(id);
    }

}

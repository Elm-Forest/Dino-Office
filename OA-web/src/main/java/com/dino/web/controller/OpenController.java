package com.dino.web.controller;

import com.dino.common.utils.LocationUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Zhang Jinming
 */
@Api(tags = "开放接口")
@RestController
@RequestMapping(value = "open")
public class OpenController {
    @ApiOperation("通过名称查找具体地址")
    @GetMapping("location")
    public Object selectLocation(String keywords) {
        return LocationUtils.getLocation(keywords);
    }
}

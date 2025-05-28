package com.ll.biztrip.domain.weather.weather.controller;


import com.ll.biztrip.domain.weather.weather.dto.WeatherInfoDto;
import com.ll.biztrip.domain.weather.weather.dto.WeatherResponseDto;
import com.ll.biztrip.domain.weather.weather.service.WeatherService;
import com.ll.biztrip.global.enums.Msg;
import com.ll.biztrip.global.rsData.RsData;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/weather")
@RequiredArgsConstructor
public class ApiV1WeatherController {

    private final WeatherService weatherService;

    @GetMapping("/trip")
    @Operation(summary = "trip 도착지 별 날씨 조회")
    @PreAuthorize("isAuthenticated()")
    public RsData<WeatherResponseDto> getWeatherByTrip(
            @RequestParam Long tripPlanId
    ){

        WeatherResponseDto weatherResponseDto = weatherService.getFormattedForecast(tripPlanId);

        return RsData.of(Msg.E200_1_INQUIRY_SUCCEED.getCode(), Msg.E200_1_INQUIRY_SUCCEED.getMsg(), weatherResponseDto);
    }

    @GetMapping("/current")
    @Operation(summary = "현재 위치 날씨 조회")
    @PreAuthorize("isAuthenticated()")
    public RsData<List<WeatherInfoDto>> getCurrentWeather(
            @RequestParam String address
    ){

        List<WeatherInfoDto> weatherInfoDtos = weatherService.getCurrentWeather(address);

        return RsData.of(Msg.E200_1_INQUIRY_SUCCEED.getCode(), Msg.E200_1_INQUIRY_SUCCEED.getMsg(), weatherInfoDtos);
    }

}

package com.ll.biztrip.domain.weather.weather.controller;


import com.ll.biztrip.domain.weather.weather.dto.WeatherResponseDto;
import com.ll.biztrip.domain.weather.weather.service.WeatherService;
import com.ll.biztrip.global.enums.Msg;
import com.ll.biztrip.global.rq.Rq;
import com.ll.biztrip.global.rsData.RsData;
import com.ll.biztrip.standard.base.Empty;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/weather")
@RequiredArgsConstructor
public class ApiV1WeatherController {

    private final WeatherService weatherService;
    private final Rq rq;

    @PostMapping("/location")
    @Operation(summary = "위치 정보 저장")
    public RsData<Empty> addLocations(){

        if(!rq.isAdmin()){
            return RsData.of(Msg.E403_0_FORBIDDEN.getCode(),Msg.E403_0_FORBIDDEN.getMsg());
        }

        weatherService.loadFromCsv();

        return RsData.of(Msg.E200_0_CREATE_SUCCEED.getCode(), Msg.E200_0_CREATE_SUCCEED.getMsg());
    }

    @GetMapping("/trip")
    @Operation(summary = "trip 도착지 별 날씨 조회")
    public RsData<WeatherResponseDto> getWeatherByTrip(
            @RequestParam Long tripPlanId
    ){

        WeatherResponseDto weatherResponseDto = weatherService.getFormattedForecast(tripPlanId);

        return RsData.of(Msg.E200_1_INQUIRY_SUCCEED.getCode(), Msg.E200_1_INQUIRY_SUCCEED.getMsg(), weatherResponseDto);
    }

}

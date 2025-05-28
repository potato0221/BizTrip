package com.ll.biztrip.domain.member.admin;

import com.ll.biztrip.domain.trip.transport.bus.service.BusService;
import com.ll.biztrip.domain.trip.transport.flight.service.FlightService;
import com.ll.biztrip.domain.trip.transport.train.service.TrainService;
import com.ll.biztrip.domain.weather.weather.service.WeatherService;
import com.ll.biztrip.global.enums.Msg;
import com.ll.biztrip.global.rsData.RsData;
import com.ll.biztrip.standard.base.Empty;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class ApiV1AdminController {

    private final BusService busService;
    private final FlightService flightService;
    private final TrainService trainService;
    private final WeatherService weatherService;

    @PostMapping("/addTerminal")
    @Operation(summary = "버스 터미널 등록")
    public RsData<Empty> addTerminal() {

        busService.updateTerminal();

        return RsData.of(Msg.E200_0_CREATE_SUCCEED.getCode(), Msg.E200_0_CREATE_SUCCEED.getMsg());
    }

    @PostMapping("/addAirport")
    @Operation(summary = "공항 등록")
    public RsData<Empty> addAirports() {

        flightService.updateAirport();

        return RsData.of(Msg.E200_0_CREATE_SUCCEED.getCode(), Msg.E200_0_CREATE_SUCCEED.getMsg());
    }

    @PostMapping("/addAirline")
    @Operation(summary = "항공사 등록")
    public RsData<Empty> addAirlines() {

        flightService.updateAirline();

        return RsData.of(Msg.E200_0_CREATE_SUCCEED.getCode(), Msg.E200_0_CREATE_SUCCEED.getMsg());
    }

    @PostMapping("/addCity")
    @Operation(summary = "기차 도시 등록")
    public RsData<Empty> addCities() {

        trainService.updateKtxCity();

        return RsData.of(Msg.E200_0_CREATE_SUCCEED.getCode(), Msg.E200_0_CREATE_SUCCEED.getMsg());
    }

    @PostMapping("/addTrainType")
    @Operation(summary = "기차 종류 등록")
    public RsData<Empty> addTrainType() {

        trainService.updateTrainType();

        return RsData.of(Msg.E200_0_CREATE_SUCCEED.getCode(), Msg.E200_0_CREATE_SUCCEED.getMsg());
    }

    @PostMapping("/addStation")
    @Operation(summary = "기차역 등록")
    public RsData<Empty> addStations() {

        trainService.updateStation();

        return RsData.of(Msg.E200_0_CREATE_SUCCEED.getCode(), Msg.E200_0_CREATE_SUCCEED.getMsg());
    }

    @PostMapping("/location")
    @Operation(summary = "위치 정보 저장")
    public RsData<Empty> addLocations(){

        weatherService.loadFromCsv();

        return RsData.of(Msg.E200_0_CREATE_SUCCEED.getCode(), Msg.E200_0_CREATE_SUCCEED.getMsg());
    }
}

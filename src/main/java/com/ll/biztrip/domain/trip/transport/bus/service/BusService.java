package com.ll.biztrip.domain.trip.transport.bus.service;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ll.biztrip.domain.member.member.entity.Member;
import com.ll.biztrip.domain.trip.transport.bus.dto.AddTerminalDto;
import com.ll.biztrip.domain.trip.transport.bus.dto.BusRegisterDto;
import com.ll.biztrip.domain.trip.transport.bus.dto.BusScheduleDto;
import com.ll.biztrip.domain.trip.transport.bus.dto.TerminalDto;
import com.ll.biztrip.domain.trip.transport.bus.entity.Bus;
import com.ll.biztrip.domain.trip.transport.bus.entity.Terminal;
import com.ll.biztrip.domain.trip.transport.bus.repository.BusRepository;
import com.ll.biztrip.domain.trip.transport.bus.repository.TerminalRepository;
import com.ll.biztrip.global.app.AppConfig;
import com.ll.biztrip.global.exceptions.GlobalException;
import com.ll.biztrip.global.msg.Msg;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BusService {

    private final TerminalRepository terminalRepository;
    private final BusRepository busRepository;

    public void updateTerminal() {
        if (terminalRepository.count() > 0) {
            System.out.println("이미 데이터가 존재합니다. 요청을 취소 합니다.");
            return;
        }

        try {
            for (int i = 1; i <= 10; i++) {
                // 1. URL 설정
                StringBuilder urlBuilder = new StringBuilder("http://apis.data.go.kr/1613000/ExpBusInfoService/getExpBusTrminlList");
                // 2. 오픈 API의요청 규격에 맞는 파라미터 생성, 발급받은 인증키.
                urlBuilder.append("?" + URLEncoder.encode("serviceKey", "UTF-8") + "=" + AppConfig.openApiKey);
                urlBuilder.append("&" + URLEncoder.encode("pageNo", "UTF-8") + "=" + URLEncoder.encode(String.valueOf(i), "UTF-8"));
                urlBuilder.append("&" + URLEncoder.encode("numOfRows", "UTF-8") + "=" + URLEncoder.encode(String.valueOf(30), "UTF-8"));
                urlBuilder.append("&" + URLEncoder.encode("_type", "UTF-8") + "=" + URLEncoder.encode("json", "UTF-8"));
                // 3. URL 객체 생성.
                System.out.println("URL 요청 확인: " + urlBuilder.toString());

                URL url = new URL(urlBuilder.toString());
                // 4. 요청하고자 하는 URL과 통신하기 위한 Connection 객체 생성.
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                // 5. 통신을 위한 메소드 SET.
                conn.setRequestMethod("GET");
                // 6. 통신을 위한 Content-type SET.
                conn.setRequestProperty("Content-type", "application/json");
                // 7. 통신 응답 코드 확인.
                System.out.print(i + "번째 통신 결과 : ");
                if (conn.getResponseCode() == 200) {
                    System.out.println("성공");
                } else {
                    System.out.println("실패");
                }
                // 8. 전달받은 데이터를 BufferedReader 객체로 저장.
                BufferedReader br;
                if (conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
                    br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                } else {
                    br = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
                }
                // 9. 저장된 데이터를 라인별로 읽어 StringBuilder 객체로 저장.
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = br.readLine()) != null) {
                    sb.append(line);
                }

                // 10. 객체 해제.
                br.close();
                conn.disconnect();

                ObjectMapper objectMapper = new ObjectMapper();

                // String -> JSON 변환
                JsonNode node1 = objectMapper.readTree(sb.toString());
                // JSON 안의 'data' 객체 추출
                JsonNode node2 = node1.path("response").path("body").path("items").path("item");

                if (node2.isMissingNode() || !node2.isArray()) {
                    System.out.println("데이터가 존재하지 않습니다.");
                    return;
                }

                // JSON엔 존재하나 DTO에는 존재하지 않는 매핑 값에 대해 처리
                objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                // DTO List 생성
                List<AddTerminalDto> addTerminalDtos = Arrays.asList(objectMapper.treeToValue(node2, AddTerminalDto[].class));

                saveTerminals(addTerminalDtos);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Transactional
    public void saveTerminals(List<AddTerminalDto> addTerminalDtos) {

        for(AddTerminalDto addTerminalDto : addTerminalDtos){

            if(terminalRepository.existsByTerminalId(addTerminalDto.getTerminalId())){
                continue;
            }

            Terminal terminal = Terminal.builder()
                    .terminalId(addTerminalDto.getTerminalId())
                    .terminalName(addTerminalDto.getTerminalName())
                    .build();

            terminalRepository.save(terminal);
            System.out.println("저장");
        }
    }

    public List<TerminalDto> getTerminals() {
        return terminalRepository.findAll(Sort.by("terminalName"))
                .stream()
                .map(TerminalDto::new)
                .collect(Collectors.toList());
    }

    public List<BusScheduleDto> getBusSchedule(String departureTerminalId, String arrivalTerminalId, LocalDate departureDate) {
        List<BusScheduleDto> allSchedules = new ArrayList<>();

        try {
            String depDateStr = departureDate.format(DateTimeFormatter.ofPattern("yyyyMMdd"));

            for (int i = 1; i <= 10; i++) {
                StringBuilder urlBuilder = new StringBuilder("http://apis.data.go.kr/1613000/ExpBusInfoService/getStrtpntAlocFndExpbusInfo");
                urlBuilder.append("?" + URLEncoder.encode("serviceKey", "UTF-8") + "=" + AppConfig.openApiKey);
                urlBuilder.append("&" + URLEncoder.encode("pageNo", "UTF-8") + "=" + i);
                urlBuilder.append("&" + URLEncoder.encode("depPlandTime", "UTF-8") + "=" + depDateStr);
                urlBuilder.append("&" + URLEncoder.encode("depTerminalId", "UTF-8") + "=" + departureTerminalId);
                urlBuilder.append("&" + URLEncoder.encode("arrTerminalId", "UTF-8") + "=" + arrivalTerminalId);
                urlBuilder.append("&" + URLEncoder.encode("numOfRows", "UTF-8") + "=" + 30);
                urlBuilder.append("&" + URLEncoder.encode("_type", "UTF-8") + "=" + "json");

                URL url = new URL(urlBuilder.toString());
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setRequestProperty("Content-type", "application/json");

                BufferedReader br = (conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300)
                        ? new BufferedReader(new InputStreamReader(conn.getInputStream()))
                        : new BufferedReader(new InputStreamReader(conn.getErrorStream()));

                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = br.readLine()) != null) sb.append(line);

                br.close();
                conn.disconnect();

                ObjectMapper objectMapper = new ObjectMapper();
                objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

                JsonNode itemNode = objectMapper.readTree(sb.toString()).path("response").path("body").path("items").path("item");

                if (itemNode.isMissingNode() || !itemNode.isArray()) {
                    break;
                }

                List<BusScheduleDto> schedules = Arrays.asList(objectMapper.treeToValue(itemNode, BusScheduleDto[].class));
                allSchedules.addAll(schedules);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return allSchedules;
    }

    public void addBusSchedule(BusRegisterDto busRegisterDto, Member member){

        if(busRepository.existsByMemberAndDepartureNameAndArrivalNameAndDepartureTimeAndArrivalTime(
                member, busRegisterDto.getDepartureName(), busRegisterDto.getArrivalName(),
                busRegisterDto.getDepartureTime(), busRegisterDto.getArrivalTime()
        )){
            throw new GlobalException(
                    Msg.E400_1_ALREADY_REGISTERED_BUS.getCode(),
                    Msg.E400_1_ALREADY_REGISTERED_BUS.getMsg());
        }

        Bus bus = Bus.builder()
                .departureName(busRegisterDto.getDepartureName())
                .arrivalName(busRegisterDto.getArrivalName())
                .departureTime(busRegisterDto.getDepartureTime())
                .arrivalTime(busRegisterDto.getArrivalTime())
                .busGrade(busRegisterDto.getBusGrade())
                .member(member)
                .build();

        busRepository.save(bus);
    }
}

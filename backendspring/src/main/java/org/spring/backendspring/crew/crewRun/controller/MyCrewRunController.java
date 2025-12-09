package org.spring.backendspring.crew.crewRun.controller;

import lombok.RequiredArgsConstructor;
import org.spring.backendspring.crew.crewRun.dto.CrewRunDto;
import org.spring.backendspring.crew.crewRun.service.CrewRunService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/mycrew/{crewId}/run")
public class MyCrewRunController {

    private final CrewRunService crewRunService;

    //크루 런닝 스케줄
    @GetMapping({"","/"})
    public ResponseEntity<?> myCrewRun(@PathVariable("crewId") Long crewId){
        List<CrewRunDto> crewRunList = crewRunService.findCrewRunList(crewId);
        Map<String,Object> crewRunListMap = new HashMap<>();
        crewRunListMap.put("crewRun", crewRunList);
        return ResponseEntity.status(HttpStatus.OK).body(crewRunListMap);
    }

    //크루 런닝 스케줄 생성
    @PostMapping("/create")
    public ResponseEntity<?> crewRunCreate(@RequestBody CrewRunDto runDto){
        CrewRunDto crewRunDto = crewRunService.crewRunCreate(runDto);
        Map<String, Object> crewRunMap = new HashMap<>();
        crewRunMap.put("crewRun", crewRunDto);
        return ResponseEntity.status(HttpStatus.OK).body(crewRunMap);
    }

    //크루 런닝 스케줄 수정
    @PostMapping("/update")
    public ResponseEntity<?> crewRunUpdate(@RequestBody CrewRunDto runDto){
        CrewRunDto crewRunDto = crewRunService.crewRunUpdate(runDto);
        Map<String, Object> crewRunMap = new HashMap<>();
        crewRunMap.put("crewRun", crewRunDto);
        return ResponseEntity.status(HttpStatus.OK).body(crewRunMap);
    }

    //크루 런닝 스케줄 상세보기
    @GetMapping("/detail/{runId}")
    public ResponseEntity<?> crewRunDetails(@PathVariable("crewId") Long crewId,
                                               @PathVariable("runId") Long runId){
        CrewRunDto crewRunDto = crewRunService.detailCrewRun(crewId, runId);
        Map<String, Object> crewRunDetail = new HashMap<>();
        crewRunDetail.put("crewRun", crewRunDto);
        return ResponseEntity.status(HttpStatus.OK).body(crewRunDetail);
    }

    //크루 런닝 스케줄 삭제
    @DeleteMapping("delete/{runId}")
    public ResponseEntity<?> crewRunDelete(@PathVariable("crewId") Long crewId,
                                           @PathVariable("runId") Long runId){
        crewRunService.crewRunDelete(crewId, runId);
        return ResponseEntity.ok("런닝스케줄 삭제완료");
    }

    
}

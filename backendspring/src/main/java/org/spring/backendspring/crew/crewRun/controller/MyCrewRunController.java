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


    @GetMapping({"","/"})
    public ResponseEntity<?> myCrewRun(@PathVariable("crewId") Long crewId){
        List<CrewRunDto> crewRunList = crewRunService.findCrewRunList(crewId);
        Map<String,Object> crewRunListMap = new HashMap<>();
        crewRunListMap.put("crewRun", crewRunList);
        return ResponseEntity.status(HttpStatus.OK).body(crewRunListMap);
    }


    @PostMapping("/create")
    public ResponseEntity<?> crewRunCreate(@RequestBody CrewRunDto runDto){
        CrewRunDto crewRunDto = crewRunService.crewRunCreate(runDto);
        Map<String, Object> crewRunMap = new HashMap<>();
        crewRunMap.put("crewRun", crewRunDto);
        return ResponseEntity.status(HttpStatus.OK).body(crewRunMap);
    }


    @PostMapping("/update")
    public ResponseEntity<?> crewRunUpdate(@RequestBody CrewRunDto runDto){
        CrewRunDto crewRunDto = crewRunService.crewRunUpdate(runDto);
        Map<String, Object> crewRunMap = new HashMap<>();
        crewRunMap.put("crewRun", crewRunDto);
        return ResponseEntity.status(HttpStatus.OK).body(crewRunMap);
    }


    @GetMapping("/detail/{runId}")
    public ResponseEntity<?> crewRunDetails(@PathVariable("crewId") Long crewId,
                                               @PathVariable("runId") Long runId){
        CrewRunDto crewRunDto = crewRunService.detailCrewRun(crewId, runId);
        Map<String, Object> crewRunDetail = new HashMap<>();
        crewRunDetail.put("crewRun", crewRunDto);
        return ResponseEntity.status(HttpStatus.OK).body(crewRunDetail);
    }


    @DeleteMapping("delete/{runId}")
    public ResponseEntity<?> crewRunDelete(@PathVariable("crewId") Long crewId,
                                           @PathVariable("runId") Long runId){
        crewRunService.crewRunDelete(crewId, runId);
        return ResponseEntity.ok("런닝스케줄 삭제완료");
    }

    
}

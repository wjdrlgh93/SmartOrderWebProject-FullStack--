package org.spring.backendspring.crew.crew.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.spring.backendspring.config.security.MyUserDetails;
import org.spring.backendspring.crew.crew.dto.CrewDto;
import org.spring.backendspring.crew.crew.service.CrewService;
import org.spring.backendspring.crew.crewMember.dto.CrewMemberDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/mycrew")
public class MyCrewController {

    private final CrewService crewService;

    @GetMapping("/list")
    public ResponseEntity<?> myCrewList(@AuthenticationPrincipal MyUserDetails myUserDetails) {
        Long memberId = myUserDetails.getMemberId();
        List<CrewMemberDto> myAllCrew = crewService.findMyAllCrew(memberId);
        Map<String, Object> myCrewList = new HashMap<>();
        myCrewList.put("myCrewList", myAllCrew);
        return ResponseEntity.status(HttpStatus.OK).body(myCrewList);
    }

    //내 크루 접속 시 기본
    @GetMapping({"/{crewId}"})
    public ResponseEntity<?> myCrew(@PathVariable("crewId") Long crewId,
                                    @AuthenticationPrincipal MyUserDetails myUserDetails){
        Long memberId = myUserDetails.getMemberId();
        CrewDto crewDto = crewService.findMyCrew(crewId, memberId);
        Map<String, Object> myCrew = new HashMap<>();
        myCrew.put("crew", crewDto);
        return ResponseEntity.status(HttpStatus.OK).body(myCrew);
    }

    @PutMapping("/{crewId}/update")
    public ResponseEntity<?> update(@PathVariable("crewId") Long crewId,
                                    @RequestPart("crewName") String crewName,
                                    @RequestPart("description") String description,
                                    @RequestPart("district") String district,
                                    @RequestParam(value = "newImages", required = false) List<MultipartFile> newImages,
                                    @RequestParam(value = "deleteImageName", required = false) List<String> deleteImageName,
                                    @AuthenticationPrincipal MyUserDetails userDetails) throws IOException {
    
        Long loginUserId = userDetails.getMemberId();
        CrewDto crewDto = new CrewDto();
        crewDto.setName(crewName);
        crewDto.setDescription(description);
        crewDto.setDistrict(district);

        CrewDto updated = crewService.updateCrew(loginUserId, crewId, crewDto, newImages, deleteImageName);

        Map<String, CrewDto> response = new HashMap<>();

        response.put("updatedCrew", updated);

        return ResponseEntity.ok(response);
    }
}

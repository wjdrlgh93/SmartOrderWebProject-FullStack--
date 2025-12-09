package org.spring.backendspring.crew.crew.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.spring.backendspring.common.dto.PagedResponse;
import org.spring.backendspring.config.security.MyUserDetails;
import org.spring.backendspring.crew.crew.dto.CrewDto;
import org.spring.backendspring.crew.crew.service.CrewService;
import org.spring.backendspring.crew.crewJoin.dto.CrewJoinRequestDto;
import org.spring.backendspring.crew.crewJoin.service.CrewJoinRequestService;
import org.spring.backendspring.crew.crewMember.dto.CrewMemberDto;
import org.spring.backendspring.crew.crewMember.service.CrewMemberService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping("/api/crew")
@RequiredArgsConstructor
public class CrewController {

    private final CrewService crewService;
    private final CrewMemberService crewMemberService;
    private final CrewJoinRequestService crewJoinRequestService;

    @PutMapping("/update/{crewId}")
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

    @DeleteMapping("/delete/{crewId}")
    public ResponseEntity<?> delete(@PathVariable("crewId") Long crewId,
                                    @AuthenticationPrincipal MyUserDetails userDetails) {

        Long loginUserId = userDetails.getMemberId();
        crewService.deleteCrew(crewId, loginUserId);

        return ResponseEntity.ok(Map.of("message", "크루 삭제 완료"));
    }

    @GetMapping("/list")
    public ResponseEntity<?> list(@RequestParam(name = "subject", required = false) String subject,
                                  @RequestParam(name = "keyword", required = false) String keyword,
                                  @RequestParam(name = "page", defaultValue = "0") int page,
                                  @RequestParam(name = "size", defaultValue = "10") int size) {

        PagedResponse<CrewDto> crewList = crewService.crewList(subject, keyword, page, size);
        
        Map<String, Object> response = new HashMap<>();

        response.put("crewList", crewList);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/detail/{crewId}")
    public ResponseEntity<?> detail(@PathVariable("crewId") Long crewId) {

        CrewDto crewDetail = crewService.crewDetail(crewId);

        Map<String, CrewDto> response = new HashMap<>();

        response.put("crewDetail", crewDetail);

        return ResponseEntity.ok(response);
    }

    //CrewsController 없애면서 CrewController 로 옮겨주세요 axios 프론트 주소도,,ㅎㅎ
    @PostMapping("/joinRequest")
    public ResponseEntity<?> crewJoinRequests(
            @RequestBody CrewJoinRequestDto joinDto){
        CrewJoinRequestDto crewJoinRequestDto = crewJoinRequestService.crewJoinRequest(joinDto);
        Map<String, Object> joinMap = new HashMap<>();
        joinMap.put("joinRequest", crewJoinRequestDto);
        return ResponseEntity.status(HttpStatus.OK).body(joinMap);
    }

    @GetMapping("mycrewList/{memberId}")
    public ResponseEntity<?> mycrewList(@PathVariable("memberId") Long memberId,
                                         @AuthenticationPrincipal MyUserDetails userDetails) {
        
          Long loginUserId = userDetails.getMemberId();
          if (!loginUserId.equals(memberId)) {
              throw new IllegalArgumentException("본인 크루 리스트는 본인만 조회 가능");
          }

        //  List<CrewDto> mycrewList = crewService.myCrewList(memberId);
         List<CrewMemberDto> mycrewList = crewMemberService.myCrewList(memberId);
        
         return ResponseEntity.ok(mycrewList);
     }

    
    
}

// package org.spring.backendspring;

// import org.junit.jupiter.api.Test;
// import org.spring.backendspring.common.RequestStatus;
// import org.spring.backendspring.crew.crew.entity.CrewEntity;
// import org.spring.backendspring.crew.crew.repository.CrewRepository;
// import org.spring.backendspring.crew.crewJoin.dto.CrewJoinRequestDto;
// import org.spring.backendspring.crew.crewJoin.entity.CrewJoinRequestEntity;
// import org.spring.backendspring.crew.crewJoin.repository.CrewJoinRequestRepository;
// import org.spring.backendspring.crew.crewJoin.service.CrewJoinRequestService;
// import org.spring.backendspring.crew.crewMember.dto.CrewMemberDto;
// import org.spring.backendspring.crew.crewMember.entity.CrewMemberEntity;
// import org.spring.backendspring.crew.crewMember.repository.CrewMemberRepository;
// import org.spring.backendspring.crew.crewMember.service.CrewMemberService;
// import org.spring.backendspring.member.entity.MemberEntity;
// import org.spring.backendspring.member.repository.MemberRepository;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.test.context.SpringBootTest;
// import org.springframework.transaction.annotation.Transactional;

// import java.util.List;
// import java.util.Optional;

// @SpringBootTest

// public class CrewTest {
//     @Autowired
//     CrewRepository crewRepository;
//     @Autowired
//     CrewJoinRequestRepository crewJoinRequestRepository;
//     @Autowired
//     CrewMemberRepository crewMemberRepository;
//     @Autowired
//     MemberRepository memberRepository;
//     @Autowired
//     CrewJoinRequestService crewJoinRequestService;
//     @Autowired
//     CrewMemberService crewMemberService;

//     @Test
//     public void insertDummyCrews() {
//         // 크루 개설자 (예: member_id = 1)
//         MemberEntity creator = memberRepository.findById(1L)
//                 .orElseThrow(() -> new IllegalArgumentException("⚠️ member_id=1 회원이 존재하지 않습니다."));

//         for (int i = 11; i <= 15; i++) {
//             CrewEntity crew = CrewEntity.builder()
//                     .name("테스트 크루 " + i)
//                     .isCrewImg(i % 2) // 0과 1 번갈아가며 설정
//                     .district("강남구")
//                     .description("이것은 테스트 크루 " + i + "의 설명입니다.")  
//                     .memberEntity(creator)
//                     .build();

//             crewRepository.save(crew);
//         }

//         System.out.println("✅ 5개의 더미 크루가 member_id=1과 함께 저장되었습니다!");
//     }


//     @Test
//     void crewList(){
//         List<CrewEntity> crewEntityList = crewRepository.findAll();
//         crewEntityList.forEach(System.out::println);

//     }

//     @Test
//     void crewJoin(){
//         Long crewId = 1L;
//         Long memberId = 4L;
//         String message =" test 통과 ";

//         //회원 맞냐?
//         MemberEntity memberEntity = memberRepository.findById(memberId)
//                 .orElseThrow(() -> new IllegalArgumentException("회원이 아닌데 어떻게 가입신청함?"));

//         //크루 있냐?
//         CrewEntity crewEntity =crewRepository.findById(crewId)
//                 .orElseThrow(() -> new IllegalArgumentException("크루없는데 어떻게 해당 크루에 가입신청함?"));

//         //이미 크루멤버냐?
//         Optional<CrewMemberEntity> optionalCrewMember = crewMemberRepository.findByCrewEntityIdAndMemberEntityId(crewId, memberId);
//         if (optionalCrewMember.isPresent()) throw new IllegalArgumentException("이미 해당 크루 멤버임");

//         //이미 신청했냐?
//         Optional<CrewJoinRequestEntity> optionalCrewJoinRequestEntity = crewJoinRequestRepository.findByCrewEntityIdAndMemberEntityId(crewId, memberId);
//         if (optionalCrewJoinRequestEntity.isPresent()) throw new IllegalArgumentException("이미 해당 크루 가입 요청 대기중임");



//         CrewJoinRequestEntity crewJoinRequestEntity = crewJoinRequestRepository.save(CrewJoinRequestEntity.insertCrewJoinRequest(CrewJoinRequestDto.builder()
//                 .crewEntity(crewEntity)
//                 .memberEntity(memberEntity)
//                 .message(message)
//                 .build()));

//     }

//     @Test
//     void crewJoinApproved() {
//         Long crewId = 1L;
//         Long memberId = 4L;
//         CrewJoinRequestEntity request = crewJoinRequestRepository.findByCrewEntityIdAndMemberEntityId(crewId, memberId)
//                 .orElseThrow(()->new NullPointerException("가입신청 없음"));

//         if (request.getStatus() != RequestStatus.PENDING)
//             throw new IllegalArgumentException("이미 처리함");

// //                    승인으로 변경
//         crewJoinRequestRepository.save(CrewJoinRequestEntity.updateCrewJoinApproved(request));

// //                    //크루 멤버 저장
//         crewMemberRepository.save(CrewMemberEntity.insertCrewMember(request));
//     }
//     @Test
//     @Transactional
//     void findCrewMember(){
//         Long crewId = 1L;
//         Long memberId = 2L;
//         Optional<CrewMemberEntity> crewMemberEntity =
//                  crewMemberRepository.findByCrewEntityIdAndMemberEntityId(crewId, memberId);
//         System.out.println(crewMemberEntity.get().getCrewEntity().getId());
//         System.out.println(crewMemberEntity.get().getMemberEntity().getId());
//         System.out.println(crewMemberEntity.get().getRoleInCrew());

//     }

//     // @Test
//     // @Transactional
//     // void crewJoinList(){
//     //     Long crewId = 1L;
//     //    List<CrewJoinRequestDto> crewJoinRequestDtos = crewJoinRequestService.myCrewJoinList(crewId);
//     //    crewJoinRequestDtos.forEach(System.out::println);
//     // }

    // @Test
    // @Transactional
    // void crewMemberList(){
    //     Long crewId = 1L;
    //     List<CrewMemberDto> crewMemberDtos  = crewMemberService.findCrewMemberList(crewId);
    //     crewMemberDtos.forEach(System.out::println);
    // }

//     @Test
//     @Transactional
//     void crewMemberDetail(){
//         Long crewId = 1L;
//         Long memberId = 2L;
//         CrewMemberDto crewMemberDtos  = crewMemberService.detailCrewMember(crewId, memberId);


//         System.out.println(crewMemberDtos);
//     }

//     @Test
//     @Transactional
//     void myCrewDetail() {
//         Optional<CrewEntity> byId = crewRepository.findById(4L);
//         List<CrewMemberEntity> list = byId.get().getCrewMemberEntities().stream().toList();
//         list.forEach(System.out::println);
//     }
// }

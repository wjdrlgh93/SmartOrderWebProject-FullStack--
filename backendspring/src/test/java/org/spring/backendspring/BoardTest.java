// package org.spring.backendspring;

// import org.junit.jupiter.api.Assertions;
// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.Test;
// import org.spring.backendspring.board.entity.BoardEntity;
// import org.spring.backendspring.board.repository.BoardRepository;
// import org.spring.backendspring.common.Gender;

// import org.spring.backendspring.common.role.MemberRole;

// import org.spring.backendspring.member.entity.MemberEntity;
// import org.spring.backendspring.member.repository.MemberRepository;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.test.context.SpringBootTest;

// import jakarta.transaction.Transactional;


// @SpringBootTest
// public class BoardTest {

//     @Autowired
//     private BoardRepository boardRepository;

//     @Autowired
//     private MemberRepository memberRepository;


//     private MemberEntity testWriter;

//     @BeforeEach 
//     void setup() {
//         // ID 1L로 직접 조회하기 전에, 먼저 존재하는지 확인하고 없으면 생성
//         testWriter = memberRepository.findById(1L).orElseGet(() -> {
//             // ID 1번 회원이 없으면, 새로 생성하여 저장
//             MemberEntity newMember = MemberEntity.builder()
//                     .userEmail("test@example.com")
//                     .userPassword("1234") // 실제로는 인코딩 필요
//                     .nickName("테스트닉네임")
//                     .userName("테스트회원")
//                     .gender(Gender.MAN) // Gender Enum에 맞게 수정
//                     .age(30)
//                     .phone("010-1234-5678")
//                     .address("테스트 주소")
//                     .role(MemberRole.MEMBER) // Role Enum에 맞게 수정
//                     .isProfileImg(0)
//                     .socialLogin(0)
//                     .build();
//             return memberRepository.save(newMember); // 저장된 엔티티 반환
//         });
//     }

//     @Test
//     void insertDummyBoards() {

   
//         MemberEntity writer = memberRepository.findById(1L).orElse(null);

//         for (int i = 1; i <= 30; i++) {

//             boardRepository.save(
//                     BoardEntity.builder()
//                             .title("테스트 게시글 제목 " + i)
//                             .content("테스트 게시글 내용 " + i)
//                             .attachFile(0)
//                             .hit(0)
//                             .memberEntity(writer) // null이어도 그냥 감
//                             .build());
//         }
//         long count = boardRepository.count();

//         System.out.println("게시글 30개 생성 완료");
//         Assertions.assertEquals(30, count, "게시글이 예상대로 30개 저장되지 않았습니다.");
//     }
// }

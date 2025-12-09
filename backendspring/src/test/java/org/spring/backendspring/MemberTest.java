//  package org.spring.backendspring;
//
//  import org.junit.jupiter.api.Test;
//  import org.spring.backendspring.common.Gender;
//  import org.spring.backendspring.common.role.MemberRole;
//  import org.spring.backendspring.member.entity.MemberEntity;
//  import org.spring.backendspring.member.repository.MemberRepository;
//  import org.springframework.beans.factory.annotation.Autowired;
//  import org.springframework.boot.test.context.SpringBootTest;
//  import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//
//  @SpringBootTest
//  public class MemberTest {
//
//      @Autowired
//      private MemberRepository memberRepository;
//
//      private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
//
//      @Test
//      public void insertDummyMembers() {
//          for (int i = 1; i <= 30; i++) {
//              MemberEntity member = MemberEntity.builder()
//                      .userEmail("user" + i + "@email.com")
//                      .userPassword(passwordEncoder.encode("1111"))
//                      .userName("테스트회원" + i)
//                      .nickName("KKWA" + i)
//                      .gender(i % 2 == 0 ? Gender.MAN : Gender.WOMAN)
//                      .age(20 + i)
//                      .phone("010-0000-000" + i)
//                      .address("서울시 테스트구 " + i + "번지")
//                      .role(MemberRole.MEMBER)
//                      .build();
//
//              memberRepository.save(member);
//          }
//
//          System.out.println("✅ 30명의 더미 회원이 성공적으로 저장되었습니다!");
//      }
//
//      @Test
//      public void insertAdminMember() {
//          MemberEntity admin = MemberEntity.builder()
//                  .userEmail("admin@email.com")
//                  .userPassword(passwordEncoder.encode("1234"))
//                  .userName("관리자")
//                  .nickName("adminNick")
//                  .gender(Gender.MAN)
//                  .age(30)
//                  .phone("010-1234-5678")
//                  .address("서울시 관리자구 1번지")
//                  .role(MemberRole.ADMIN)
//                  .build();
//
//          memberRepository.save(admin);
//          System.out.println("✅ 관리자 계정이 성공적으로 저장되었습니다!");
//      }
//  }

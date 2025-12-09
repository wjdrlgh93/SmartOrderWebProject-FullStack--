//  package org.spring.backendspring;


//  import org.junit.jupiter.api.Test;
//  import org.spring.backendspring.item.entity.ItemEntity;
//  import org.spring.backendspring.item.repository.ItemRepository;
//  import org.spring.backendspring.member.entity.MemberEntity;
//  import org.spring.backendspring.member.repository.MemberRepository;
//  import org.springframework.beans.factory.annotation.Autowired;
//  import org.springframework.boot.test.context.SpringBootTest;

//  @SpringBootTest
//  public class ItemTest {

//      @Autowired
//      private ItemRepository itemRepository;

//      @Autowired
//      private MemberRepository memberRepository;

//      @Test
//      public void insertDummyItems() {
//          // 등록자(회원) 1명 가져오기
//          MemberEntity member = memberRepository.findById(1L)
//                  .orElseThrow(() -> new IllegalArgumentException("⚠️ member_id=1 회원이 존재하지 않습니다."));

//          for (int i = 1; i <= 30; i++) {
//              ItemEntity item = ItemEntity.builder()
//                      .itemTitle("테스트 상품 " + i)
//                      .itemDetail("이것은 테스트용 상품 상세 설명입니다. 번호: " + i)
//                      .itemPrice(10000 + (i * 500)) // 점점 비싸지게
//                      .itemSize(250 + (i % 10)) // 예: 사이즈 250~259
//                      .attachFile(0)
//                      .category("Cloth")
//                      .newFileName(null)
//                      .oldFileName(null)
//                      .memberEntity(member) // 등록자 지정
//                      .build();

//              itemRepository.save(item);
//          }

//          System.out.println("✅ 30개의 더미 상품이 member_id=1로 성공적으로 저장되었습니다!");
//      }
//  }

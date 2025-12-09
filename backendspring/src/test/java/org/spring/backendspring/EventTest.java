// package org.spring.backendspring;

// import org.junit.jupiter.api.Test;
// import org.spring.backendspring.event.entity.EventEntity;
// import org.spring.backendspring.event.repository.EventRepository;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.test.context.SpringBootTest;

// import java.time.LocalDateTime;

// @SpringBootTest
// public class EventTest {

//     @Autowired
//     private EventRepository eventRepository;

//     @Test
//     public void insertDummyEvents() {

//         for (int i = 1; i <= 45; i++) {

//             EventEntity event = EventEntity.builder()
//                     .eventTitle("테스트 이벤트 " + i)
//                     .eventLink("https://example.com/event/" + i)
//                     .eventDescription("이것은 이벤트 " + i + "의 설명입니다.")
//                     .startDate(LocalDateTime.now().minusDays(i))
//                     .endDate(LocalDateTime.now().plusDays(i))
//                     .createTime(LocalDateTime.now())
//                     .updateTime(LocalDateTime.now())
//                     .build();

//             eventRepository.save(event);
//         }

//         System.out.println("✅ 45개의 테스트 이벤트가 성공적으로 저장되었습니다!");
//     }
// }

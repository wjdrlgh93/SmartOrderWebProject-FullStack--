package org.spring.backendspring.rabbitmqWebsocket.chat.rabbitmqService;

import org.spring.backendspring.crew.crewBoard.repository.CrewBoardRepository;
import org.spring.backendspring.crew.crewRun.entity.CrewRunEntity;
import org.spring.backendspring.crew.crewRun.entity.CrewRunMemberEntity;
import org.spring.backendspring.crew.crewRun.repository.CrewRunMemberRepository;
import org.spring.backendspring.crew.crewRun.repository.CrewRunRepository;
import org.spring.backendspring.rabbitmqWebsocket.chat.dto.BotMessageDto;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.shineware.nlp.komoran.constant.DEFAULT_MODEL;
import kr.co.shineware.nlp.komoran.core.Komoran;
import kr.co.shineware.nlp.komoran.model.KomoranResult;
import kr.co.shineware.nlp.komoran.model.Token;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Service
@RequiredArgsConstructor
@Log4j2
public class MyCrewBotService {
    @Value("${spring.rabbitmq.crew.exchange}")
    private String crewExchangeYml;
    
    private final CrewRunRepository crewRunRepository;
    private final CrewBoardRepository crewBoardRepository;
    private final CrewRunMemberRepository crewRunMemberRepository;

    private final RabbitTemplate rabbitTemplate;
    private final Komoran komoran = new Komoran(DEFAULT_MODEL.FULL);

    //형태소 분석 오류 해결 
    private String komoranGoText(String komoranText) {
        if (komoranText == null) return "";
    
        // 시간 표현들 미리 정리
        komoranText = komoranText.replace("이번 주", "이번주");
        komoranText = komoranText.replace("저번 주", "저번주");
        komoranText = komoranText.replace("다음 주", "다음주");
        komoranText = komoranText.replace("이번 달", "이번달");
        komoranText = komoranText.replace("이번 달에", "이번달"); 
        komoranText = komoranText.replace("몇 개", "몇개"); 
        komoranText = komoranText.replace("몇 번", "몇번");
        komoranText = komoranText.replace("다음 번", "다음번");
    
        return komoranText;
    }
    @Transactional(readOnly = true)
    public void sendCrewBot(BotMessageDto botMessageDto) {
        Long crewId = botMessageDto.getCrewId();
        Long memberId = botMessageDto.getMemberId();
        //사용자가 보낸 택스트 
        String komoranText = botMessageDto.getText() ;

        //위에 메서드
        String komoranGoGoText = komoranGoText(komoranText);

        //코모란
        KomoranResult komoranResult = komoran.analyze(komoranGoGoText);
        List<Token> tokens = komoranResult.getTokenList();

        //라우딩키인데 사실 별 의미는 없음 crew.#임 구독을 {crewId}.{memberId}로 해서
        String routingKey = "crew." + crewId + "." + memberId;

        //봇 메시지
        String text = "";

        //기간설정,데이터찾기를 위한 선언 미리 하기
        LocalDate dateToday = LocalDate.now();
        LocalDateTime dateStart;
        LocalDateTime dateEnd;
        
        //if로 체크 하기위한 참 거짓
        boolean hi = false; //인사
        boolean me = false; //나 본인
        boolean today = false; //오늘
        boolean runSchedule = false; //런닝 일정
        boolean board = false; // 게시글
        boolean thisWeek = false; // 이번주
        boolean thisMonth = false;// 이번달
        boolean thisCount = false;// 몇개 몇번
        boolean thisNext = false;// 다음

        //문자열 빌더
        StringBuilder sb = new StringBuilder();

        for (Token token : tokens) {
            String botMsgNnp = token.getMorph();
            log.info("====={}=====", botMsgNnp);
            
            // 초기 접속했을때나 인사
            if (List.of("안녕", "하이", "헬로", "hello","hellow", "ㅎㅇ").contains(botMsgNnp)) hi = true;

            // 나, 본인
            if (List.of("내", "나", "본인").contains(botMsgNnp)) me = true;

            //시간
            if (botMsgNnp.equals("오늘")) today = true;
            // if (botMsgNnp.equals("이번")) botThis = true;
            if (List.of("이번주", "금주", "이번").contains(botMsgNnp)) thisWeek = true;
            if (botMsgNnp.equals("이번달")) thisMonth = true;
            if (List.of("다음", "다음번").contains(botMsgNnp)) thisNext = true;

            //정보
            if (List.of("런","런닝", "일정", "스케줄").contains(botMsgNnp)) runSchedule = true;
            if (List.of("글", "게시글", "게시물").contains(botMsgNnp)) board = true;

            //카운트
            if (List.of("몇개", "몇번", "몇").contains(botMsgNnp)) thisCount = true;
        }
        // ========================================
        //            ex) 인사, 초기
        // ========================================
        if (hi) {
            text =  "어서오세요!" + botMessageDto.getMemberNickName() 
            + "님 궁금한 정보 있으시면 물어봐주세요 🚀" + "\n" ;

        }  
        // ========================================
        //            ex) 내 이번주 런닝
        // ========================================
            else if (me && thisCount && runSchedule) {
                Long runCount ;
                if (today) {
                    dateStart = dateToday.atStartOfDay();
                    dateEnd = dateToday.plusDays(1).atStartOfDay();
                    runCount = crewRunMemberRepository.countByMemberEntityIdAndCrewRunEntityCrewEntityIdAndCrewRunEntityStartAtBetween(memberId, crewId, dateStart, dateEnd);
                    sb.append("오늘의 런닝 일정은" + runCount + "개 입니다.");
                } else if (thisWeek) {
                    LocalDate firstDayOfWeek = dateToday.with(DayOfWeek.MONDAY);   // 이번 주 월요일
                    dateStart = firstDayOfWeek.atStartOfDay();      // 이번 주 월요일 0시
                    dateEnd = firstDayOfWeek
                    .plusWeeks(1)                  // 다음 주 월요일
                    .atStartOfDay();
                    runCount = crewRunMemberRepository.countByMemberEntityIdAndCrewRunEntityCrewEntityIdAndCrewRunEntityStartAtBetween(memberId, crewId, dateStart, dateEnd);
                    sb.append("이번주의 런닝 일정은" + runCount + "개 입니다.");
                    
                } else if (thisMonth) {
                    LocalDate firstDayOfMonth = dateToday.withDayOfMonth(1);   // 이번 달 1일
                    dateStart = firstDayOfMonth.atStartOfDay(); ;
                    dateEnd = firstDayOfMonth
                    .plusMonths(1)             // 다음 달 1일
                    .atStartOfDay();           // 다음 달 1일 0시
                    runCount = crewRunMemberRepository.countByMemberEntityIdAndCrewRunEntityCrewEntityIdAndCrewRunEntityStartAtBetween(memberId, crewId, dateStart, dateEnd);
                    sb.append("이번달의 런닝 일정은" + runCount + "개 입니다.");
                } else {
                    sb.append("언제 기준인지 모르겠어요! 오늘/이번주/이번달 중에서 물어봐 주세요 😊.");
                }
                text = sb.toString();
            } 
        // ========================================
        //            ex) 내 다음 런닝 , 내 런닝
        // ======================================== 
            else if (me && runSchedule) {
                LocalDateTime now = LocalDateTime.now();
                if (thisNext) {
                    Optional<CrewRunMemberEntity> nextRun = crewRunMemberRepository
                    .findFirstByMemberEntityIdAndCrewRunEntityCrewEntityIdAndCrewRunEntityStartAtAfterOrderByCrewRunEntityStartAtAsc(memberId, crewId, now);
                    if (!nextRun.isPresent()) {
                        sb.append( "내 다음 런닝이 없습니다");
                        
                    } else { 
                        CrewRunEntity run = nextRun.get().getCrewRunEntity();
                        Long runMemberCount = crewRunMemberRepository.countByCrewRunEntityId(run.getId()); 
                        sb.append("내 다음 런닝은 🏃" + "\n" +
                                    "시간 : " + run.getStartAt() + " ~ "+
                                        run.getEndAt() + "\n " +
                                    "제목 : " + run.getTitle() + "\s" +
                                    "/ 장소 : " + run.getPlace() + "\s" +
                                    "/ 코스 : " + run.getRouteHint() + "\s" +
                                    "/ 참여원 : " + runMemberCount + "명"
                                    +"\n" + "\n");
                        }
                } else {
                    List<CrewRunMemberEntity> nextRunList = crewRunMemberRepository
                    .findByMemberEntityIdAndCrewRunEntityCrewEntityIdAndCrewRunEntityStartAtAfterOrderByCrewRunEntityStartAtAsc(memberId, crewId, now);
                    if (nextRunList.isEmpty()) {
                        sb.append(botMessageDto.getMemberNickName() + "님이 참여하신 런닝일정이 없습니다.");
                    }
                    for (CrewRunMemberEntity run : nextRunList) {
                        
                        CrewRunEntity meRun = run.getCrewRunEntity();
                        Long runMemberCount = crewRunMemberRepository.countByCrewRunEntityId(meRun.getId()); 
                        sb.append("시간 : " + meRun.getStartAt() + " ~ "+
                                meRun.getEndAt() + "\n " +
                                "제목 : " + meRun.getTitle() + "\s" +
                                "/ 장소 : " + meRun.getPlace() + "\s" +
                                "/ 코스 : " + meRun.getRouteHint() + "\s" +
                                "/ 참여원 : " + runMemberCount + "명"
                                +"\n" + "\n");
                    }
                }
                text = sb.toString();
                
            } 
        // ========================================
        //          ex) 오늘 이번주 이번달 런닝 
        // ======================================== 
            else if (runSchedule) { // 기간별 런닝일정
                
                List<CrewRunEntity> dateRunList = List.of();

                if (today) { //오늘
                    //시간대 설정    
                    dateStart = dateToday.atStartOfDay();
                    dateEnd = dateToday.plusDays(1).atStartOfDay();
                    //일정 리스트
                    dateRunList =
                    crewRunRepository.findByCrewEntityIdAndStartAtBetween(crewId, dateStart, dateEnd);
                    sb.append("오늘 런닝 일정은 총" +
                        dateRunList.size() + "개 있습니다" + "\n"+"\n") ;

                } else if (thisWeek) { //이번주
                     //시간대 설정    
                    LocalDate firstDayOfWeek = dateToday.with(DayOfWeek.MONDAY);   // 이번 주 월요일
                    dateStart = firstDayOfWeek.atStartOfDay();      // 이번 주 월요일 0시
                    dateEnd = firstDayOfWeek
                                .plusWeeks(1)                  // 다음 주 월요일
                                .atStartOfDay();
                    dateRunList =
                    crewRunRepository.findByCrewEntityIdAndStartAtBetween(crewId, dateStart, dateEnd);
                    sb.append("이번주 런닝 일정은 총" +
                    dateRunList.size() + "개 있습니다" + "\n"+"\n") ;

                } else if (thisMonth) { //이번달
                    LocalDate firstDayOfMonth = dateToday.withDayOfMonth(1);   // 이번 달 1일
                    dateStart = firstDayOfMonth.atStartOfDay(); ;
                    dateEnd = firstDayOfMonth
                                .plusMonths(1)             // 다음 달 1일
                                .atStartOfDay();           // 다음 달 1일 0시
    
                    //일정 리스트
                    dateRunList =
                    crewRunRepository.findByCrewEntityIdAndStartAtBetween(crewId, dateStart, dateEnd);
                    sb.append("이번달 런닝 일정은 총" +
                            dateRunList.size() + "개 있습니다" + "\n"+"\n") ;

                } 

                if (dateRunList.isEmpty()) {
                    text =  "언제 기준인지 모르겠어요! 오늘/이번주/이번달 중에서 물어봐 주세요 😊." ;
                } else {
                    
                    for (CrewRunEntity run : dateRunList) {
                        Long runMemberCount = crewRunMemberRepository.countByCrewRunEntityId(run.getId());
                        sb.append("시간 : " + run.getStartAt() + " ~ "+
                                 run.getEndAt() + "\n " +
                                "제목 : " + run.getTitle() + "\s" +
                                "/ 장소 : " + run.getPlace() + "\s" +
                                "/ 코스 : " + run.getRouteHint() + "\s" +
                                "/ 참여원 : " + runMemberCount + "명"
                                +"\n" + "\n");
                    }
                    text = sb.toString();
                }
            }   
            else {
                    text = "등록 되어있지 않은 정보 입니다 :( "+
                    "\n"+
                    "추후에 더 추가 예정 노다가라 힘들다 ";
            }

        
        BotMessageDto botMessageDto2 = BotMessageDto.builder()
        .crewId(botMessageDto.getCrewId())
        .memberId(botMessageDto.getMemberId())
        .memberNickName(botMessageDto.getMemberNickName())
        .text(text)
        .build();

        rabbitTemplate.convertAndSend(crewExchangeYml, routingKey, botMessageDto2);
    }


}

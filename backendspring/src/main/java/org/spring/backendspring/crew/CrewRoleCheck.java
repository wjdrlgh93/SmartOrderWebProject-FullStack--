package org.spring.backendspring.crew;

import java.util.Optional;
import org.spring.backendspring.crew.crew.entity.CrewEntity;
import org.spring.backendspring.crew.crew.repository.CrewRepository;
import org.spring.backendspring.crew.crewMember.entity.CrewMemberEntity;

public class CrewRoleCheck {

    // 크루 권한을 추출하는 메서드입니다. (회원 id, 가입한 크루 id, crewRepository)
    public static String crewRoleCheckFn(Long memberId,
                                         Long crewId,
                                         CrewRepository crewRepository) {
        String crewRole = "";
        Optional<CrewEntity> crewEntity = crewRepository.findById(crewId);
        crewRole = crewEntity.get().getCrewMemberEntities().stream()
                .filter(entity -> entity.getMemberId().equals(memberId))
                .map(CrewMemberEntity::getRoleInCrew)
                .findFirst()
                .map(Enum::name)
                .orElse("");
        return crewRole;
    }
}

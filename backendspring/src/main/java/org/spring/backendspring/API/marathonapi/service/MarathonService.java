package org.spring.backendspring.API.marathonapi.service;

import java.util.List;
import org.spring.backendspring.API.marathonapi.entity.Marathon;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MarathonService {
    // 기존 메서드 (사용하지 않을 수도 있지만, 일단 유지)
    List<Marathon> findAll();
    
    // ⭐️ [추가] 검색어와 페이징을 포함한 조회 메서드
    Page<Marathon> findMarathons(String searchTerm, Pageable pageable);
}
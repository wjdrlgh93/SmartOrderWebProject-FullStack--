package org.spring.backendspring.API.marathonapi.repository;

import org.spring.backendspring.API.marathonapi.entity.Marathon;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MarathonRepository extends JpaRepository<Marathon, Long> {
    
    // ⭐️ [추가] 대회명(name) 또는 대회장소(location)에 검색어(Containing)가 포함된 항목을 찾고 페이징 처리합니다.
    Page<Marathon> findByNameContainingOrLocationContaining(String name, String location, Pageable pageable);
}
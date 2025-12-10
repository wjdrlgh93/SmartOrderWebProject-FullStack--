package org.spring.backendspring.API.marathonapi.repository;

import org.spring.backendspring.API.marathonapi.entity.Marathon;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MarathonRepository extends JpaRepository<Marathon, Long> {
    

    Page<Marathon> findByNameContainingOrLocationContaining(String name, String location, Pageable pageable);
}
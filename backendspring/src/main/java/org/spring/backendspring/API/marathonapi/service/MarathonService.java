package org.spring.backendspring.API.marathonapi.service;

import java.util.List;
import org.spring.backendspring.API.marathonapi.entity.Marathon;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MarathonService {

    List<Marathon> findAll();
    

    Page<Marathon> findMarathons(String searchTerm, Pageable pageable);
}
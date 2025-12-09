package org.spring.backendspring.admin.repository;

import org.spring.backendspring.member.entity.MemberEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.util.Streamable;

public interface AdminMemberRepository extends JpaRepository<MemberEntity, Long> {

    // search 용도
    Page<MemberEntity> findByUserEmailContainingIgnoreCaseOrNickNameContainingIgnoreCase(
            String emailKeyword,
            String nickKeyword,
            Pageable pageable);

    @Query("SELECT COUNT(m) FROM MemberEntity m")
    long countAll();

    @Query("SELECT COUNT(m) FROM MemberEntity m WHERE DATE(m.createTime) = CURRENT_DATE")
    long countToday();

    Page<MemberEntity> findByIsDeletedFalse(Pageable pageable);

    Page<MemberEntity> findByUserEmailContainingAndIsDeletedFalse(Pageable pageable, String search);

    Page<MemberEntity> findByUserNameContainingAndIsDeletedFalse(Pageable pageable, String search);

    Page<MemberEntity> findByNickNameContainingAndIsDeletedFalse(Pageable pageable, String search);

}
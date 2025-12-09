package org.spring.backendspring.admin.service.impl;

import java.util.List;

import org.spring.backendspring.admin.dto.AdminMemberDto;
import org.spring.backendspring.admin.repository.AdminMemberRepository;
import org.spring.backendspring.admin.service.AdminMemberService;
import org.spring.backendspring.common.dto.PagedResponse;
import org.spring.backendspring.crew.crew.entity.CrewEntity;
import org.spring.backendspring.member.MemberMapper;
import org.spring.backendspring.member.dto.MemberDto;
import org.spring.backendspring.member.entity.MemberEntity;
import org.spring.backendspring.member.repository.MemberRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminMemberServiceImpl implements AdminMemberService {

    private final AdminMemberRepository adminMemberRepository;
    private final MemberRepository memberRepository;

    // 멤버 전체 조회
    @Override
    public PagedResponse<MemberDto> findAllMembers(String search, String subject, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
        Page<MemberEntity> memberEntities;

        if (subject == null || search == null || search.trim().isEmpty()) {
            // 검색어 없을 때 → 전체조회
            memberEntities = adminMemberRepository.findByIsDeletedFalse(pageable);
        } else if (subject.equals("userEmail")) {
            memberEntities = adminMemberRepository.findByUserEmailContainingAndIsDeletedFalse(pageable, search);
        } else if (subject.equals("userName")) {
            memberEntities = adminMemberRepository.findByUserNameContainingAndIsDeletedFalse(pageable, search);
        } else if (subject.equals("nickName")) {
            memberEntities = adminMemberRepository.findByNickNameContainingAndIsDeletedFalse(pageable, search);
        } else {
            memberEntities = adminMemberRepository.findByIsDeletedFalse(pageable);
        }
        return PagedResponse.of(memberEntities.map(MemberMapper::toDtoList));
    }

    @Override
    public MemberDto updateMemberByAdmin(Long id, AdminMemberDto memberDto) {
        MemberEntity memberEntity = memberRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하는 회원이 아닙니다."));
        MemberEntity member = memberRepository.save(MemberMapper.toAdminMemberUpdate(memberDto, memberEntity));
        return MemberMapper.toDto(member);
    }

    @Override
    public void deleteMemberByAdmin(Long id) {
        MemberEntity member = memberRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("멤버를 찾을 수 없습니다."));
        memberRepository.delete(member);
    }
}

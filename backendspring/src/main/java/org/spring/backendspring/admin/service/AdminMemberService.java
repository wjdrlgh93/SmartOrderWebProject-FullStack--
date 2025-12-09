package org.spring.backendspring.admin.service;

import java.util.List;

import org.spring.backendspring.admin.dto.AdminMemberDto;
import org.spring.backendspring.common.dto.PagedResponse;
import org.spring.backendspring.member.dto.MemberDto;

public interface AdminMemberService {

    public PagedResponse<MemberDto> findAllMembers(String search, String subject, int page, int size);

    MemberDto updateMemberByAdmin(Long id, AdminMemberDto memberDto);

    void deleteMemberByAdmin(Long id);
}

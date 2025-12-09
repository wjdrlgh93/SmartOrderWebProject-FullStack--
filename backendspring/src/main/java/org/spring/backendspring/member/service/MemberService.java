package org.spring.backendspring.member.service;

import java.io.IOException;
import org.spring.backendspring.member.dto.MemberDto;
import org.springframework.web.multipart.MultipartFile;

public interface MemberService  {

    void insertMember(MemberDto memberDto, MultipartFile memberFile) throws IOException;

    MemberDto findByUserEmail(String userEmail);

    MemberDto findById(Long id) throws IOException;

    MemberDto updateMember(Long id, MemberDto updatedDto, MultipartFile memberFile) throws IOException;

    void deleteMember(Long id);

    int userEmailCheck(String userEmail);
}
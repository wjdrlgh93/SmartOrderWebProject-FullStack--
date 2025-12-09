package org.spring.backendspring.member.service.impl;

import java.io.File;
import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

import org.spring.backendspring.config.s3.AwsS3Service;
import org.spring.backendspring.crew.crewCreate.repository.CrewCreateRequestRepository;
import org.spring.backendspring.crew.crewJoin.repository.CrewJoinRequestRepository;
import org.spring.backendspring.member.MemberMapper;
import org.spring.backendspring.member.dto.MemberDto;
import org.spring.backendspring.member.entity.MemberEntity;
import org.spring.backendspring.member.entity.MemberProfileImageEntity;
import org.spring.backendspring.member.repository.MemberProfileImageRepository;
import org.spring.backendspring.member.repository.MemberRepository;
import org.spring.backendspring.member.service.MemberService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final MemberProfileImageRepository memberProfileImageRepository;
    private final AwsS3Service awsS3Service;

    @Value("${s3file.path.member}")
    public String path;

    @Override
    public int userEmailCheck(String userEmail) {
        Optional<MemberEntity> byUserEmail = memberRepository.findByUserEmail(userEmail);
        if (byUserEmail.isEmpty()) {
            return 0;
        }

        if (byUserEmail.get().isDeleted()) {
            return 2;
        } else {
            return 1;
        }
    }

    @Override
    public MemberDto findByUserEmail(String userEmail) {
        return memberRepository.findByUserEmail(userEmail)
                .map(MemberMapper::toDto)
                .orElse(null);
    }

    @Override
    public void insertMember(MemberDto memberDto, MultipartFile memberFile) throws IOException {

        memberRepository.findByUserEmail(memberDto.getUserEmail())
                .ifPresent((email) -> {
                    throw new IllegalArgumentException("이미 존재하는 이메일입니다. " + email);
                });

        MemberEntity memberEntity = MemberMapper.toEntity(memberDto, passwordEncoder);
        memberEntity.setIsProfileImg(memberFile != null ? 1 : 0);
        memberRepository.save(memberEntity);

        if (memberFile != null) {
            String newFileName = awsS3Service.uploadFile(memberFile, path);

            memberProfileImageRepository.save(
                    MemberProfileImageEntity.builder()
                            .oldName(memberFile.getOriginalFilename())
                            .newName(newFileName)
                            .memberEntity(memberEntity)
                            .build());
        }
    }

    // 개인(본인) 회원 조회
    @Override
    public MemberDto findById(Long id) throws IOException {
        MemberDto memberDto = memberRepository.findById(id)
                .map(MemberMapper::toDto)
                .orElseThrow(() -> new EntityNotFoundException("해당 회원이 존재하지 않습니다"));
        if (memberDto.getIsProfileImg() == 1) {
            String fileUrl = awsS3Service.getFileUrl(memberDto.getProfileImagesList().get(0).getNewName());
            memberDto.setFileUrl(fileUrl);
        }
        return memberDto;

    }

    @Override
    public MemberDto updateMember(Long id, MemberDto updatedDto, MultipartFile memberFile) throws IOException {
        MemberEntity memberEntity = memberRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("해당 회원이 존재하지 않습니다"));

        if (updatedDto.getUserPassword() != null) {
            memberEntity.setUserPassword(passwordEncoder.encode(updatedDto.getUserPassword()));
        }

        if (memberFile != null) {
            String newFileName = awsS3Service.uploadFile(memberFile, path);

            Optional<MemberProfileImageEntity> memberImg =
                    memberProfileImageRepository.findByMemberEntity_id(memberEntity.getId());

            if (memberImg.isEmpty()) {
                memberProfileImageRepository.save(MemberProfileImageEntity.builder()
                        .oldName(memberFile.getOriginalFilename())
                        .newName(newFileName)
                        .memberEntity(memberEntity)
                        .build());
                memberEntity.setIsProfileImg(1);
            } else {
                String oldFile = memberImg.get().getNewName();
                if (!oldFile.isEmpty()) {
                    awsS3Service.deleteFile(oldFile);
                }

                memberImg.get().setOldName(memberFile.getOriginalFilename());
                memberImg.get().setNewName(newFileName);
                memberImg.get().setMemberEntity(memberEntity);
            }
        } else {
            memberProfileImageRepository.findByMemberEntity_id(memberEntity.getId())
                    .ifPresent(memberImg -> {
                        String oldFile = memberImg.getNewName();
                        if (!oldFile.isEmpty()) {
                            awsS3Service.deleteFile(oldFile);
                        }
                        memberProfileImageRepository.delete(memberImg);
                    });
        }
        MemberEntity updateEntity = MemberMapper.toUpdateEntity(updatedDto, memberEntity);
        return MemberMapper.toDto(memberRepository.save(updateEntity));
    }

    @Override
    public void deleteMember(Long id) {
        MemberEntity member = memberRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("해당 회원이 존재하지 않습니다"));
        if (member.isDeleted()) {
            throw new IllegalArgumentException("이미 탈퇴한 회원입니다.");
        }
        MemberMapper.toDeleteSet(member);
    }
}

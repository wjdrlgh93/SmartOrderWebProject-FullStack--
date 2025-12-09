package org.spring.backendspring.member.service.impl;

import org.spring.backendspring.member.dto.MemberProfileImageDto;
import org.spring.backendspring.member.entity.MemberEntity;
import org.spring.backendspring.member.entity.MemberProfileImageEntity;
import org.spring.backendspring.member.repository.MemberProfileImageRepository;
import org.spring.backendspring.member.repository.MemberRepository;
import org.spring.backendspring.member.service.MemberProfileImageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;



import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberProfileImageServiceImpl implements MemberProfileImageService {


    private final MemberRepository memberRepository;
    private final MemberProfileImageRepository memberProfileImageRepository;

    @Value("${spring.cloud.aws.s3.bucket}")
    private String bucketName;

    @Override
    public MemberProfileImageDto findByMemberEntity_id(Long memberId) {
        return memberProfileImageRepository.findByMemberEntity_id(memberId).map(MemberProfileImageEntity::toDto)
                .orElse(null);
    }

    @Override
    public void uploadProfileImage(MultipartFile file, Long memberId) {

        MemberEntity memberEntity = memberRepository.findById(memberId).orElseThrow(() -> new RuntimeException("존재하지 않는 회원입니다."));

        memberEntity.setIsProfileImg(1);
        memberRepository.save(memberEntity);

        String originalName = file.getOriginalFilename();
        String newName =System.currentTimeMillis() + "_" + originalName;
        String key = "profile/" + newName;


        try{
            // amazonS3.putObject(bucketName, key, file.getInputStream(), null);

            MemberProfileImageEntity profileImageEntity = MemberProfileImageEntity.builder()
            .oldName(originalName)
            .newName(newName)
            .memberEntity(memberEntity)
            .build();

            
            
            memberProfileImageRepository.save(profileImageEntity);

        }catch (Exception e) {
            throw new RuntimeException("파일 업로드에 실패했습니다.");
        }
    }
}

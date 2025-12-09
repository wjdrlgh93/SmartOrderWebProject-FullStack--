package org.spring.backendspring.member;

import java.time.LocalDateTime;
import java.util.List;
import org.spring.backendspring.admin.dto.AdminMemberDto;
import org.spring.backendspring.common.Gender;
import org.spring.backendspring.common.role.MemberRole;
import org.spring.backendspring.member.dto.MemberDto;
import org.spring.backendspring.member.entity.MemberEntity;
import org.spring.backendspring.member.entity.MemberProfileImageEntity;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;

public class MemberMapper {

    // 일반 회원가입
    public static MemberEntity toEntity(MemberDto dto,
                                        PasswordEncoder passwordEncoder) {
        return MemberEntity.builder()
                .id(dto.getId())
                .userEmail(dto.getUserEmail())
                .userPassword(passwordEncoder.encode(dto.getUserPassword()))
                .userName(dto.getUserName())
                .nickName(dto.getNickName())
                .gender(dto.getGender())
                .age(dto.getAge())
                .phone(dto.getPhone())
                .address(dto.getAddress())
                .role(dto.getRole())
                .isProfileImg(dto.getIsProfileImg())
                .socialLogin(0)
                .build();
    }

    // 회원 불러오기
    public static MemberDto toDto(MemberEntity entity) {
        MemberDto.MemberDtoBuilder memberDtoBuilder = MemberDto.builder()
                .id(entity.getId())
                .userEmail(entity.getUserEmail())
                .userName(entity.getUserName())
                .nickName(entity.getNickName())
                .gender(entity.getGender())
                .age(entity.getAge())
                .phone(entity.getPhone())
                .address(entity.getAddress())
                .role(entity.getRole())
                .crewEntityList(entity.getCrewEntityList())
                .crewMemberEntityList(entity.getCrewMemberEntityList())
                .socialLogin(entity.getSocialLogin())
                .createTime(entity.getCreateTime())
                .updateTime(entity.getUpdateTime());

        List<MemberProfileImageEntity> profileImagesList = entity.getProfileImagesList();
        if (profileImagesList != null && !profileImagesList.isEmpty()) {
            MemberProfileImageEntity profileImage = profileImagesList.get(0);

            memberDtoBuilder
                    .isProfileImg(1)
                    .newFileName(profileImage.getNewName())
                    .profileImagesList(profileImagesList);
        } else {
            memberDtoBuilder
                    .isProfileImg(0);
        }

        if (profileImagesList != null && !profileImagesList.isEmpty()) {
            memberDtoBuilder.isProfileImg(1)
                    .profileImagesList(profileImagesList);
        }

        return memberDtoBuilder.build();
    }

    // 회원 update
    public static MemberEntity toUpdateEntity(MemberDto memberDto,
                                              MemberEntity memberEntity) {
        return MemberEntity.builder()
                .id(memberEntity.getId())
                .userEmail(memberEntity.getUserEmail())
                .userPassword(memberEntity.getUserPassword())
                .userName(memberDto.getUserName())
                .nickName(memberDto.getNickName())
                .age(memberDto.getAge())
                .gender(memberEntity.getGender())
                .address(memberDto.getAddress())
                .phone(memberDto.getPhone())
                .socialLogin(memberEntity.getSocialLogin())
                .role(memberEntity.getRole())
                .isProfileImg(memberEntity.getIsProfileImg())
                .build();
    }

    // 회원 list
    public static MemberDto toDtoList(MemberEntity entity) {
        return MemberDto.builder()
                .id(entity.getId())
                .userEmail(entity.getUserEmail())
                .userName(entity.getUserName())
                .nickName(entity.getNickName())
                .gender(entity.getGender())
                .age(entity.getAge())
                .phone(entity.getPhone())
                .address(entity.getAddress())
                .role(entity.getRole())
                .isProfileImg(entity.getIsProfileImg())
                .socialLogin(entity.getSocialLogin())
                .createTime(entity.getCreateTime())
                .build();
    }

    // admin 회원 업데이트
    // MemberEntity 새 객체 만들어서 기존 memberEntity 다 정보 넣고
    // 수정된 memberDto 넣는게 너무 비효율적인거 같아서 set 사용했아요
    public static MemberEntity toAdminMemberUpdate(AdminMemberDto memberDto,
                                                   MemberEntity memberEntity) {
        memberEntity.setUserName(memberDto.getUserName());
        memberEntity.setNickName(memberDto.getNickName());
        memberEntity.setAddress(memberDto.getAddress());
        memberEntity.setPhone(memberDto.getPhone());
        memberEntity.setAge(memberDto.getAge());
        memberEntity.setUpdateTime(LocalDateTime.now());
        return memberEntity;
    }

    // 회원 탈퇴 처리
    public static MemberEntity toDeleteSet(MemberEntity memberEntity) {
        memberEntity.setDeleted(true);
        memberEntity.setUserName("탈퇴회원");
        memberEntity.setNickName("탈퇴회원");
        memberEntity.setAddress("탈퇴회원");
        memberEntity.setPhone("010-0000-0000");
        memberEntity.setAge(1);
        return memberEntity;
    }

    // 소셜 로그인 처리
    public static MemberEntity toSocialEntity(String userEmail,
                                              String userName,
                                              String userPassword) {
        return MemberEntity.builder()
                .userEmail(userEmail)
                .userName(userName)
                .userPassword(userPassword)
                .isProfileImg(0)
                .nickName("닉네임을 입력해주세요.")
                .gender(Gender.MAN)
                .age(10)
                .phone("01000000000")
                .address("주소를 입력해주세요.")
                .role(MemberRole.MEMBER)
                .socialLogin(1)
                .build();
    }
}

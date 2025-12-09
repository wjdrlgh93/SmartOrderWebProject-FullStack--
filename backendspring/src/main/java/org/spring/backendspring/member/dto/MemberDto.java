package org.spring.backendspring.member.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.spring.backendspring.common.Gender;
import org.spring.backendspring.common.role.MemberRole;
import org.spring.backendspring.crew.crew.dto.CrewDto;
import org.spring.backendspring.crew.crew.entity.CrewEntity;
import org.spring.backendspring.crew.crewMember.entity.CrewMemberEntity;
import org.spring.backendspring.member.entity.MemberEntity;
import org.spring.backendspring.member.entity.MemberProfileImageEntity;
import org.springframework.security.crypto.password.PasswordEncoder;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class MemberDto {

    private Long id;

    @NotBlank(message = "이메일은 필수 입력 사항입니다.")
    @Email(message = "이메일 형식이 올바르지 않습니다.")
    private String userEmail;

    @NotBlank(message = "비밀번호는 필수 입력 값입니다.")
    @Size(min = 4, message = "비밀번호는 최소 4자 이상이어야 합니다.")
    private String userPassword;

    @NotBlank(message = "이름은 필수 입력 값입니다.")
    private String userName;

    @NotBlank(message = "닉네임은 필수 입력 값입니다.")
    private String nickName;

    private MemberRole role = MemberRole.MEMBER;

//    @NotBlank(message = "성별은 필수 입력 값입니다.")
    private Gender gender;

    @NotNull(message = "나이는 필수 입력 값입니다.")
    @Min(value = 1, message = "나이는 1 이상이어야 합니다.")
    private int age;

    @NotBlank(message = "전화번호는 필수 입력 값입니다.")
    private String phone;

    @NotBlank(message = "주소는 필수 입력 값입니다.")
    private String address;

    private int socialLogin;

    private int isProfileImg;

    private String newFileName;
    private String fileUrl;

    private List<MemberProfileImageEntity> profileImagesList;

    private LocalDateTime createTime;
    private LocalDateTime updateTime;

    @JsonIgnore
    private List<CrewEntity> crewEntityList;

    private List<CrewDto> crewDtoList;

    private List<CrewMemberEntity> crewMemberEntityList;
    
}

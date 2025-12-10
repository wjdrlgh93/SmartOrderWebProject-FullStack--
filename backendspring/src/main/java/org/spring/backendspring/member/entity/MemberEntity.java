package org.spring.backendspring.member.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.hibernate.annotations.SQLRestriction;
import org.spring.backendspring.board.entity.BoardEntity;
import org.spring.backendspring.board.entity.BoardReplyEntity;
import org.spring.backendspring.common.BasicTime;
import org.spring.backendspring.common.Gender;
import org.spring.backendspring.common.role.MemberRole;
import org.spring.backendspring.crew.crew.entity.CrewEntity;
import org.spring.backendspring.crew.crewMember.entity.CrewMemberEntity;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "member_tb")
public class MemberEntity extends BasicTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @Column(name = "user_email", unique = true)
    private String userEmail; // 로그인 이메일

    @Column(name = "user_password", nullable = false)
    private String userPassword; // 비밀번호

    @Column(nullable = false)
    private String nickName; // 닉네임

    @Column(nullable = false)
    private String userName; // 실명

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Gender gender; // MAN / WOMAN

    @Column(nullable = false)
    private int age; // 나이

    @Column(nullable = false)
    private String phone; // 전화번호

    @Column(nullable = false)
    private String address; // 주소

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MemberRole role; // USER/MANAGER/ADMIN

    @Column(name = "is_profile_img")
    private int isProfileImg; // 0/1

    private int socialLogin; // 0/1

    private boolean isDeleted; // 탈퇴한 회원 -> 1






    @OneToMany(mappedBy = "memberEntity", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<MemberProfileImageEntity> profileImagesList;






    @OneToMany(mappedBy = "memberEntity", fetch = FetchType.LAZY)
    private List<BoardEntity> boardEntityList;

    @OneToMany(mappedBy = "memberEntity", fetch = FetchType.LAZY)
    private List<BoardReplyEntity> boardCommentEntityList;


    @JsonIgnore
    @OneToMany(mappedBy = "memberEntity", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<CrewEntity> crewEntityList;


     @OneToMany(mappedBy = "memberEntity", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
     private List<CrewMemberEntity> crewMemberEntityList;



















}

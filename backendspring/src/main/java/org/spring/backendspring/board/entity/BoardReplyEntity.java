package org.spring.backendspring.board.entity;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.SQLRestriction;
import org.spring.backendspring.board.dto.BoardReplyDto;
import org.spring.backendspring.common.BasicTime;
import org.spring.backendspring.member.entity.MemberEntity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "board_comment_tb" )
@SQLRestriction("member_id IN (SELECT m.member_id FROM member_tb m WHERE m.is_deleted = FALSE)")
public class BoardReplyEntity extends BasicTime {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_comment_id")
    private Long id;
 
    @Column(nullable = false)
    private String content;


    // createTime 
    // updateTime 

    // N:1 
    @ManyToOne
    @Fetch(FetchMode.JOIN)
    @JoinColumn(name = "board_id")
    @JsonIgnore
    private BoardEntity boardEntity;

    // N:1
    @ManyToOne
    @JoinColumn(name = "member_id")
    @JsonIgnore
    private MemberEntity memberEntity;


    public static BoardReplyEntity toReplyEntity (BoardReplyDto boardReplyDto){

        return BoardReplyEntity.builder()
                            .id(boardReplyDto.getId())
                            .content(boardReplyDto.getContent())
                            .boardEntity(boardReplyDto.getBoardEntity())
                            .memberEntity(boardReplyDto.getMemberEntity())
                            .build();
    }


    // DTO is Empty? if Fill content-> then Update
    public void updateFromDto(BoardReplyDto dto) {
       if (dto.getContent() != null) {
        this.content = dto.getContent();
    }
    }


}

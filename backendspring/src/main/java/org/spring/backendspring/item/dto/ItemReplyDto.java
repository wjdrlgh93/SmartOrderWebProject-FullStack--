package org.spring.backendspring.item.dto;

import java.time.LocalDateTime;

import org.spring.backendspring.item.entity.ItemEntity;
import org.spring.backendspring.item.entity.ItemReplyEntity;
import org.spring.backendspring.member.entity.MemberEntity;

import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemReplyDto {

    private Long id;

    private String content;

    // item_tb
    private Long itemId;
    // member_tb
    private Long memberId;


    private LocalDateTime createTime;
    private LocalDateTime updateTime;

    private MemberEntity memberEntity;
    private ItemEntity itemEntity;

    
    public static ItemReplyDto toItemReplyDto (ItemReplyEntity itemReplyEntity){
        return ItemReplyDto.builder()
                        .id(itemReplyEntity.getId())
                        .content(itemReplyEntity.getContent())
                        .itemId(itemReplyEntity.getItemEntity().getId())
                        .memberId(itemReplyEntity.getMemberEntity().getId())
                        .createTime(itemReplyEntity.getCreateTime())
                        .updateTime(itemReplyEntity.getUpdateTime())
                    .build();

    }
    
}

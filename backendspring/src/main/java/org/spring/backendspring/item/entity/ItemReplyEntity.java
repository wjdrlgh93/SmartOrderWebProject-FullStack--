package org.spring.backendspring.item.entity;

import org.spring.backendspring.common.BasicTime;
import org.spring.backendspring.item.dto.ItemReplyDto;
import org.spring.backendspring.member.entity.MemberEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
@Table(name = "item_comment_tb" )
public class ItemReplyEntity extends BasicTime {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_comment_id")
    private Long id;

    @Column(nullable = false)
    private String content;


    // N:1 
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private MemberEntity memberEntity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private ItemEntity itemEntity;


    public static ItemReplyEntity toReplyEntity (ItemReplyDto itemReplyDto){
        return ItemReplyEntity.builder()
                            .id(itemReplyDto.getId())
                            .content(itemReplyDto.getContent())
                            .memberEntity(itemReplyDto.getMemberEntity())
                            .itemEntity(itemReplyDto.getItemEntity())

        .build();
    }

    public void updateFromDto(ItemReplyDto dto) {
        if (dto.getContent() != null) {
            this.content = dto.getContent();
        }
    }
}

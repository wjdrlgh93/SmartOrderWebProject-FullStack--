package org.spring.backendspring.board.entity;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.spring.backendspring.board.dto.BoardImgDto;
import org.spring.backendspring.common.BasicTime;
import org.spring.backendspring.item.entity.ItemEntity;
import org.spring.backendspring.item.entity.ItemImgEntity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
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
@Table(name = "board_image_tb" )
public class BoardImgEntity extends BasicTime{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_image_id", unique = true)
    private Long id;
    

    @Column(nullable = false)
    private String newName;
    @Column(nullable = false)
    private String oldName;

    // createTime
    // updateTime

    // N:1 
    @ManyToOne
    @Fetch(FetchMode.JOIN)
    @JoinColumn(name = "board_id")
    @JsonIgnore
    private BoardEntity boardEntity;

    public static BoardImgEntity toInsertFile(BoardImgDto boardImgDto){

      return BoardImgEntity.builder()
              .oldName(boardImgDto.getOldName())
              .newName(boardImgDto.getNewName())
              .boardEntity(boardImgDto.getBoardEntity())
              .build();
    }
    
    public static BoardImgEntity toBoardImgEntity(BoardEntity boardEntity, String originalFilename, String newFileName) {

    BoardImgEntity boardImgEntity = new BoardImgEntity();
    boardImgEntity.setBoardEntity(boardEntity);
    boardImgEntity.setOldName(originalFilename);
    boardImgEntity.setNewName(newFileName);
    return boardImgEntity;

  }

 

    
}

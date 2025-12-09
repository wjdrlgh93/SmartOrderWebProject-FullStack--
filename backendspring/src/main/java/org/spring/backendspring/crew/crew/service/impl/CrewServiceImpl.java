package org.spring.backendspring.crew.crew.service.impl;

import lombok.RequiredArgsConstructor;

import org.spring.backendspring.common.dto.PagedResponse;
import org.spring.backendspring.common.role.CrewRole;
import org.spring.backendspring.common.role.MemberRole;
import org.spring.backendspring.config.s3.AwsS3Service;
import org.spring.backendspring.crew.CrewRoleCheck;
import org.spring.backendspring.crew.crew.dto.CrewDto;
import org.spring.backendspring.crew.crew.entity.CrewEntity;
import org.spring.backendspring.crew.crew.entity.CrewImageEntity;
import org.spring.backendspring.crew.crew.repository.CrewImageRepository;
import org.spring.backendspring.crew.crew.repository.CrewRepository;
import org.spring.backendspring.crew.crew.service.CrewService;
import org.spring.backendspring.crew.crewMember.dto.CrewMemberDto;
import org.spring.backendspring.crew.crewMember.entity.CrewMemberEntity;
import org.spring.backendspring.crew.crewMember.repository.CrewMemberRepository;
import org.spring.backendspring.member.entity.MemberEntity;
import org.spring.backendspring.member.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CrewServiceImpl implements CrewService {

    private final CrewRepository crewRepository;
    private final CrewMemberRepository crewMemberRepository;
    private final CrewImageRepository crewImageRepository;
    private final MemberRepository memberRepository;
    private final AwsS3Service awsS3Service;

    @Value("${s3file.path.crew}")
    private String path;

    @Override
    public CrewDto updateCrew(Long loginUserId, Long crewId, CrewDto crewDto,
                              List<MultipartFile> newImages,
                              List<String> deleteImageName) throws IOException {

        
        CrewEntity crew = crewRepository.findById(crewId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 크루"));

        MemberEntity member = memberRepository.findById(loginUserId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원"));

        String crewRole = CrewRoleCheck.crewRoleCheckFn(loginUserId, crewId, crewRepository);

        if (!crewRole.equals(CrewRole.LEADER.toString()) && !member.getRole().equals(MemberRole.ADMIN)) {
            throw new IllegalArgumentException("크루 수정 권한 없음");    
        }

        crewDto.setMemberId(member.getId());

        // 크루 기본 정보 수정
        crew.setName(crewDto.getName());
        crew.setDescription(crewDto.getDescription());
        crew.setDistrict(crewDto.getDistrict());

        List<CrewImageEntity> currentImages = crewImageRepository.findByCrewEntity(crew);

        List<CrewImageEntity> updatedImages = new ArrayList<>();

        // 삭제할 이미지
        if (deleteImageName != null && !deleteImageName.isEmpty()) {
            for (String imageName : deleteImageName) {
                CrewImageEntity imageEntity = crewImageRepository.findByNewName(imageName)
                        .orElseThrow(() -> new IllegalArgumentException("이미지가 존재하지 않음"));
                awsS3Service.deleteFile(imageEntity.getNewName());
                crewImageRepository.delete(imageEntity);
            }
        }

        updatedImages = crewImageRepository.findByCrewEntity(crew);

        // 새로운 이미지
        if (newImages != null && !newImages.isEmpty()) {
            for (MultipartFile image : newImages) {
                if (!image.isEmpty()) {
                    try {
                        String originalFileName = image.getOriginalFilename();
                        String newFileName = awsS3Service.uploadFile(image, path); // S3 업로드
                        CrewImageEntity imageEntity = CrewImageEntity.toEntity(crew, originalFileName, newFileName);
                        CrewImageEntity savedImage = crewImageRepository.save(imageEntity);
                        updatedImages.add(savedImage);
                    } catch (IOException e) {
                        throw new IllegalArgumentException("S3 파일 업로드 실패", e);
                    }
                    // UUID uuid = UUID.randomUUID();
                    // String originalFileName = image.getOriginalFilename();
                    // String newFileName = uuid + "_" + originalFileName;

                    // String filePath = "E:/full/upload/" + newFileName;
                    // File file = new File(filePath);
                    // image.transferTo(file);
                    
                    // CrewImageEntity imageEntity = CrewImageEntity.toEntity(crew, originalFileName, newFileName);
                    // CrewImageEntity savedImage = crewImageRepository.save(imageEntity);
                    // updatedImages.add(savedImage);
                }
            }
        }
        crew.setCrewImageEntities(updatedImages);
        CrewEntity updated = crewRepository.save(crew);
        List<String> updatedFileUrl = new ArrayList<>();
        for (CrewImageEntity img : updatedImages) {
            if (img.getNewName() != null || !img.getNewName().isEmpty()) {
                try {
                String url = awsS3Service.getFileUrl(img.getNewName());
                updatedFileUrl.add(url);                
                } catch (IOException e) {
                    throw new IllegalArgumentException("S3 URL 생성 실패", e);
                }
            }
        }
        System.out.println("%%%%%%"+updatedFileUrl);
        return CrewDto.toCrewDto2(updated, updatedFileUrl);
    }

    @Override
    public void deleteCrew(Long crewId, Long loginUserId) {
        CrewEntity crewEntity = crewRepository.findById(crewId)
                .orElseThrow(IllegalArgumentException::new);
        MemberEntity member = memberRepository.findById(loginUserId)
                .orElseThrow(() -> new NullPointerException("존재하지 않는 회원"));
        
        String crewRole = CrewRoleCheck.crewRoleCheckFn(loginUserId, crewId, crewRepository);
        if (!crewRole.equals("LEADER") || !member.getRole().equals(MemberRole.ADMIN)) {
            throw new IllegalArgumentException("크루 삭제 권한 없음");    
        }

        List<CrewImageEntity> crewImages = crewImageRepository.findByCrewEntity_Id(crewId);
        if (crewImages != null) {
            for (CrewImageEntity img : crewImages) {
                awsS3Service.deleteFile(img.getNewName());
            }
        }

        crewImageRepository.deleteAll(crewImages);
        crewRepository.delete(crewEntity);
    }

    @Override
    public PagedResponse<CrewDto> crewList(String subject, String keyword, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        
        Page<CrewEntity> crewEntities;
        
        if (subject == null || keyword == null || keyword.trim().isEmpty()) {
            crewEntities = crewRepository.findAll(pageRequest);
        } else {
            if (subject.equals("크루명")) {
                crewEntities = crewRepository.findByNameContaining(keyword, pageRequest);
            } else if (subject.equals("크루소개")) {
                crewEntities = crewRepository.findByDescriptionContaining(keyword, pageRequest);
            } else if (subject.equals("지역")) {
                crewEntities = crewRepository.findByDistrictContaining(keyword, pageRequest);
            } else if (subject.equals("전체")) {
                crewEntities = crewRepository.findByNameContainingOrDescriptionContainingOrDistrictContaining(keyword, keyword, keyword, pageRequest);
            } else {
                crewEntities = crewRepository.findAll(pageRequest);
            }
        }        
       
        Page<CrewDto> crewList = crewEntities.map(crew -> {
            CrewDto crewDto = CrewDto.toCrewDto(crew);

            if (crewDto.getNewFileName() != null && !crewDto.getNewFileName().isEmpty()) {
                List<String> fileUrls = new ArrayList<>();
                for (String fileName : crewDto.getNewFileName()) {
                    try {
                        String url = awsS3Service.getFileUrl(fileName);
                        fileUrls.add(url);
                    } catch (IOException e) {
                        throw new IllegalArgumentException(e);
                    }

                }
                crewDto.setFileUrl(fileUrls);
            }
            return crewDto;
        });

        return PagedResponse.of(crewList);
    }

    @Override
    public CrewDto crewDetail(Long crewId) {
        CrewEntity crewEntity = crewRepository.findById(crewId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 크루"));

        CrewDto dto = CrewDto.toCrewDto(crewEntity);
        if (dto.getNewFileName() != null && !dto.getNewFileName().isEmpty()) {
            List<String> urls = new ArrayList<>();

            for (String fileName : dto.getNewFileName()) {
                try {
                    String url = awsS3Service.getFileUrl(fileName);
                    urls.add(url);
                } catch (IOException e) {
                    throw new IllegalArgumentException(e);
                }
            }
            dto.setFileUrl(urls);
        }
        return dto;
    }

    // @Override
    // public CrewDto findCrew(Long crewId) {
    //     CrewEntity crewEntity = crewRepository.findById(crewId)
    //                  .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 크루"));
    //     return CrewDto.toCrewDto(crewEntity);
    // }

    @Override
    public List<CrewDto> findAllCrew() {
        return crewRepository.findAll().stream().map(crew -> CrewDto.toCrewDto(crew)).collect(Collectors.toList());
    }

    @Override
    public List<CrewMemberDto> findMyAllCrew(Long memberId) {
        List<CrewMemberEntity> crewMemberEntities = crewMemberRepository.findByMemberEntity_id(memberId);

        List<CrewMemberDto> dtoList = new ArrayList<>();

        for (CrewMemberEntity entity : crewMemberEntities) {
            CrewMemberDto dto = CrewMemberDto.toCrewMember(entity);

            if (dto.getCrewImages() != null && !dto.getCrewImages().isEmpty()) {
                List<String> urls = new ArrayList<>();

                for (String fileName : dto.getCrewImages()) {
                    try {
                        String url = awsS3Service.getFileUrl(fileName);
                        urls.add(url);
                    } catch (IOException e) {
                        throw new IllegalArgumentException(e);
                    }
                }
                dto.setFileUrl(urls);
            }
            dtoList.add(dto);
        }

        return dtoList;
    }

    @Override
    public CrewDto findMyCrew(Long crewId, Long memberId) {
        CrewEntity crewEntity = crewRepository.findById(crewId)
                .orElseThrow(() -> new IllegalArgumentException("크루를 찾을 수 없습니다."));

        crewEntity.getCrewMemberEntities().stream()
                .filter(member -> member.getMemberId().equals(memberId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("이 크루의 멤버가 아닙니다."));

        CrewDto dto = CrewDto.toCrewDto(crewEntity);

        if (dto.getNewFileName() != null && !dto.getNewFileName().isEmpty()) {
            List<String> urls = new ArrayList<>();

            for (String fileName : dto.getNewFileName()) {
                try {
                    String url = awsS3Service.getFileUrl(fileName);
                    urls.add(url);
                } catch (IOException e) {
                    throw new IllegalArgumentException(e);
                }
            }
            dto.setFileUrl(urls);
        }
        return dto;
    }

    @Override
    public List<CrewDto> myCrewList(Long memberId) {
        MemberEntity member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원"));
        
        List<CrewEntity> mycrewList = member.getCrewEntityList();

        return mycrewList.stream()
                    .map(crew -> CrewDto.toCrewDto(crew))
                    .toList();
    }
}
//package com.example.sideproject.domain.search.dto;
//
//import com.example.sideproject.domain.search.entity.ProjectDocument;
//import lombok.Builder;
//import lombok.Getter;
//
//import java.util.List;
//
//@Getter
//@Builder
//public class SearchResultDto {
//    private Long id;
//    private String title;
//    private String content;
//    private String type;
//    private List<String> techStacks;
//
//    public static SearchResultDto fromProjectDocument(ProjectDocument document) {
//        return SearchResultDto.builder()
//                .id(document.getId())
//                .title(document.getTitle())
//                .content(document.getContent())
//                .techStacks(document.getTechStackNames())
//                .type("project")
//                .build();
//    }
//}

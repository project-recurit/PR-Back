package com.example.sideproject.domain.search.entity;


import com.example.sideproject.domain.project.entity.Project;
import com.example.sideproject.domain.project.entity.ProjectTechStack;
import com.example.sideproject.domain.techstack.entity.TechStack;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Document(indexName = "projects")
@NoArgsConstructor
@Getter
public class ProjectDocument {
    @Id
    private Long id;

    @Field(type = FieldType.Text, analyzer = "nori")
    private String title;

    @Field(type = FieldType.Text, analyzer = "nori")
    private String content;

    @Field(type = FieldType.Keyword)
    private List<String> techStackNames;

    @Field(type = FieldType.Keyword)
    private String period;

    public ProjectDocument(Project project) {
        this.id = project.getId();
        this.title = project.getTitle();
        this.content = project.getContent();
        this.techStackNames = project.getProjectTechStacks().stream()
                .map(pts -> pts.getTechStack().getName())
                .collect(Collectors.toList());
    }

}

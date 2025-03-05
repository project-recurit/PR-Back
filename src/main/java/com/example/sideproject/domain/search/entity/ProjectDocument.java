package com.example.sideproject.domain.search.entity;


import com.example.sideproject.domain.project.entity.Project;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Document(indexName = "projects")
@NoArgsConstructor
@Getter
public class ProjectDocument {
    @Id
    private Long id;

    @Field(type = FieldType.Text, analyzer = "standard")
    private String title;

    @Field(type = FieldType.Text, analyzer = "standard")
    private String content;

    public ProjectDocument(Project project) {
        this.id = project.getId();
        this.title = project.getTitle();
        this.content = project.getContent();
    }

}

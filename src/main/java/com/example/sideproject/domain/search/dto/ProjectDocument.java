package com.example.sideproject.domain.search.dto;


import com.example.sideproject.domain.project.entity.Project;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Document(indexName = "projects")
@NoArgsConstructor
@Data
public class ProjectDocument {
    @Id
    private Long id;

    @Field(type = FieldType.Text, analyzer = "standard")
    private String title;

    @Field(type = FieldType.Text, analyzer = "standard")
    private String content;

    public static ProjectDocument fromProject(Project project) {
        ProjectDocument doc = new ProjectDocument();
        doc.setId(project.getId());
        doc.setTitle(project.getTitle());
        doc.setContent(project.getContent());
        return doc;
    }


}

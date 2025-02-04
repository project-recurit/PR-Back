package com.example.sideproject.domain.project.service;

import com.example.sideproject.domain.project.entity.Project;
import com.example.sideproject.domain.project.entity.ProjectUrl;
import com.example.sideproject.domain.project.repository.ProjectUrlRepository;
import com.example.sideproject.global.enums.ErrorType;
import com.example.sideproject.global.exception.CustomException;
import com.uploadcare.api.Client;
import com.uploadcare.upload.FileUploader;
import com.uploadcare.upload.UploadFailureException;
import com.uploadcare.upload.Uploader;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Service
@RequiredArgsConstructor
public class ProjectUrlService {

    private final ProjectUrlRepository projectUrlRepository;

//    @Value("{}")
    String publicKey = "118dc3fd15ac6dfac31f";
    String secretKey = "87ceaa26a823e4d64c7f";

    public void createProjectUrl(Project project,MultipartFile multipartFile) throws IOException {

        Client client = new Client(publicKey, secretKey);

        File file = convertMultipartFileToFile(multipartFile);

        Uploader uploader = new FileUploader(client, file);
        try {
            com.uploadcare.api.File ucUpload = uploader.upload().save();

            String stringUrl = ucUpload.getOriginalFileUrl().toString();

            ProjectUrl projectUrl = ProjectUrl.builder()
                    .project(project)
                    .imageUrl(stringUrl)
                    .build();
            projectUrlRepository.save(projectUrl);

        } catch (UploadFailureException e) {
            throw new RuntimeException(e);
        }
    }

    private File convertMultipartFileToFile(MultipartFile multipartFile) throws IOException {
        File convFile = File.createTempFile("upload_", multipartFile.getOriginalFilename());
        convFile.deleteOnExit();
        multipartFile.transferTo(convFile);
        return convFile;
    }

}

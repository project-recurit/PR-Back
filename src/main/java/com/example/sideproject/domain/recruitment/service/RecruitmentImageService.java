package com.example.sideproject.domain.recruitment.service;

import com.example.sideproject.domain.recruitment.entity.Recruitment;
import com.example.sideproject.domain.recruitment.entity.RecruitmentImage;
import com.example.sideproject.domain.recruitment.repository.RecruitmentImageRepository;
import com.example.sideproject.global.enums.ErrorType;
import com.example.sideproject.global.exception.CustomException;
import com.uploadcare.api.Client;
import com.uploadcare.upload.FileUploader;
import com.uploadcare.upload.UploadFailureException;
import com.uploadcare.upload.Uploader;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RecruitmentImageService {

    private final RecruitmentImageRepository recruitmentImageRepository;

    @Value("${UPLOAD_CARE_PUB}")
    private String publicKey;
    @Value("${UPLOAD_CARE_SEC}")
    private String secretKey;

    public RecruitmentImage createRecruitmentImage(Recruitment recruitment, MultipartFile multipartFile) {

        Client client = new Client(publicKey, secretKey);

        File file = convertMultipartFileToFile(multipartFile);

        Uploader uploader = new FileUploader(client, file);
        try {
            com.uploadcare.api.File ucUpload = uploader.upload().save();

            String stringUrl = ucUpload.getOriginalFileUrl().toString();

            RecruitmentImage recruitmentImage = RecruitmentImage.builder()
                    .recruitment(recruitment)
                    .imageUrl(stringUrl)
                    .build();
            recruitmentImageRepository.save(recruitmentImage);

            return recruitmentImage;
        } catch (UploadFailureException e) {
            throw new RuntimeException(e);
        }
    }

    // 기존 구인 글에 있던 url들 조회
    public List<RecruitmentImage> existImageUrls(Long recruitmentId) {
        return recruitmentImageRepository.findAllByRecruitmentId(recruitmentId);
    }

    // 기존 구인 글에 있던 url 삭제
    public void deleteImages(List<Long> ImageIds) {
        recruitmentImageRepository.deleteAllById(ImageIds);
    }

    private File convertMultipartFileToFile(MultipartFile multipartFile) {
        // 허용된 파일 크기 (10MB)
        final long MAX_FILE_SIZE = 10 * 1024 * 1024;  // 10MB 제한

        // 허용된 확장자 목록
        List<String> allowedExtensions = Arrays.asList("png", "svg", "jpg", "jpeg");

        // 파일 이름 가져오기
        String originalFilename = multipartFile.getOriginalFilename();
        if (originalFilename == null || originalFilename.isEmpty()) {
            throw new CustomException(ErrorType.NOT_STORED_FILE_NAME);
        }

        // 확장자 추출 및 확인
        String fileExtension = getFileExtension(originalFilename).toLowerCase();
        if (!allowedExtensions.contains(fileExtension)) {
            throw new CustomException(ErrorType.NOT_ALLOWED_EXTENSION);
        }

        // 파일 크기 확인
        if (multipartFile.getSize() > MAX_FILE_SIZE) {
            throw new CustomException(ErrorType.OVER_LOAD);
        }
        File convFile = null;
        try{
            // 파일 변환
            convFile = File.createTempFile("upload_", originalFilename);
            convFile.deleteOnExit();
            multipartFile.transferTo(convFile);
        }catch(IOException e) {
            throw new CustomException(ErrorType.FILE_CONVERSION_FAILED);
        }


        return convFile;
    }

    // 확장자 추출 메서드
    private String getFileExtension(String filename) {
        int lastIndexOfDot = filename.lastIndexOf(".");
        if (lastIndexOfDot == -1 || lastIndexOfDot == filename.length() - 1) {
            return "";  // 확장자가 없는 경우
        }
        return filename.substring(lastIndexOfDot + 1);
    }
}

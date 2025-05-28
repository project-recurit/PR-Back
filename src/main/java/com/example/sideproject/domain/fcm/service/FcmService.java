package com.example.sideproject.domain.fcm.service;

import com.example.sideproject.domain.fcm.dto.DeviceDto;
import com.example.sideproject.domain.user.entity.User;
import com.example.sideproject.global.enums.ErrorType;
import com.example.sideproject.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FcmService {
    @Value("${api.server.notification.url}")
    private String apiServerUrl;

    public void saveToken(User user, String token) {
        DeviceDto request = new DeviceDto(user.getId(), token);

        RestClient restClient = RestClient.create();
        restClient.post()
                .uri(apiServerUrl + "/api/v1/fcm/device")
                .contentType(MediaType.APPLICATION_JSON)
                .body(request)
                .retrieve()
                .onStatus(status -> status.value() != 200, (req, res) -> {
                    if (res.getStatusCode().equals(HttpStatus.BAD_REQUEST)) {
                        throw new CustomException(ErrorType.FCM_TOKEN_EXISTS);
                    }

                    throw new CustomException(ErrorType.FCM_TOKEN_ERROR);
                })
                .toBodilessEntity();
    }

    public void deleteToken(User user, String token) {
        String uri = "user/" + user.getId() + "?token=" + token;
        RestClient restClient = RestClient.create();
        restClient.delete()
                .uri(apiServerUrl + "/api/v1/fcm/device/" + uri)
                .retrieve()
                .onStatus(status -> status.value() != 204, (req, res) -> {
                    if (res.getStatusCode().equals(HttpStatus.BAD_REQUEST)) {
                        throw new CustomException(ErrorType.FCM_TOKEN_NOT_FOUND);
                    }

                    throw new CustomException(ErrorType.FCM_TOKEN_ERROR);
                })
                .toBodilessEntity();
    }

    public List<String> getTokens(User user) {
        RestClient restClient = RestClient.create();
        return restClient.get()
                .uri(apiServerUrl + "/api/v1/fcm/device/user/" + user.getId())
                .retrieve()
                .onStatus(status -> status.value() != 200, (req, res) -> {
                    throw new CustomException(ErrorType.FCM_TOKEN_ERROR);
                })
                .body(new ParameterizedTypeReference<>() {});
    }
}

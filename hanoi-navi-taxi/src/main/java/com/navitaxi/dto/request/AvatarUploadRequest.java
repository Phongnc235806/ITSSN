package com.navitaxi.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * Multipart request for uploading a real profile avatar image.
 */
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class AvatarUploadRequest {

    @NotNull
    private MultipartFile file;
}

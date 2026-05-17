package com.navitaxi.dto.response;

import lombok.*;

/**
 * Response returned after the avatar image is stored and linked to the user profile.
 */
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class AvatarUploadResponse {

    private String avatarUrl;
    private String originalFilename;
    private String contentType;
    private long sizeBytes;
}

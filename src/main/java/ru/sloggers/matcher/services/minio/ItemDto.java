package ru.sloggers.matcher.services.minio;

import lombok.Builder;

@Builder
public record ItemDto(String objectName,
                      Long size,
                      String userTags,
                      Boolean isDir) {
}

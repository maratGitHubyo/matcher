package ru.sloggers.matcher.dto;

import lombok.Builder;

@Builder
public record ItemDto(String objectName,
                      Long size,
                      String userTags,
                      Boolean isDir) {
}

package ru.sloggers.matcher.mappers;


import io.minio.messages.Item;
import org.mapstruct.Mapper;
import ru.sloggers.matcher.dto.ItemDto;

@Mapper(componentModel = "spring")
public interface ItemMapper {

    default ItemDto toDto(Item item) {
        return item == null ? null : ItemDto.builder()
                .objectName(item.objectName())
                .size(item.size())
                .userTags(item.userTags())
                .isDir(item.isDir())
                .build();
    }
}

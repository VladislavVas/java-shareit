package ru.practicum.shareit.requests.dto;

import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.requests.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor
@Component
public class ItemRequestMapper {



    public static ItemRequest toItemRequest(ItemRequestDto itemRequestDto, User requester) {
        return ItemRequest.builder()
                .date(itemRequestDto.getCreated())
                .description(itemRequestDto.getDescription())
                .requester(requester)
                .build();
    }

    public static ItemRequestDto toItemRequestDto(ItemRequest itemRequest) {
        return ItemRequestDto.builder()
                .id(itemRequest.getId())
                .description(itemRequest.getDescription())
                .requesterId(itemRequest.getId())
                .created(itemRequest.getDate())
                .items(itemRequest.getItems().stream()
                        .map(ItemMapper::toDto)
                        .collect(Collectors.toList()))
                .build();
    }

    public static List<ItemRequestDto> toItemRequestDtoList(List<ItemRequest> requests) {
        return requests.stream()
                .map(ItemRequestMapper::toItemRequestDto)
                .collect(Collectors.toList());
    }
}


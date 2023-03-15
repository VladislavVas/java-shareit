package ru.practicum.shareit.requests.service;

import ru.practicum.shareit.requests.dto.ItemRequestDto;

import java.util.List;

public interface ItemRequestService {
    ItemRequestDto addRequest(ItemRequestDto itemRequestDto, long requesterId);

    ItemRequestDto getRequest(long requestId, long userId);

    List<ItemRequestDto> getAllRequestsForRequester(long requesterId);

    List<ItemRequestDto> getAllRequests(long requesterId, int from, int size);
}

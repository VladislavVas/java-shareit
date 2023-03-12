package ru.practicum.shareit.requests.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidateException;
import ru.practicum.shareit.requests.dto.ItemRequestDto;
import ru.practicum.shareit.requests.dto.ItemRequestMapper;
import ru.practicum.shareit.requests.model.ItemRequest;
import ru.practicum.shareit.requests.storage.ItemRequestStorage;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserStorage;

import java.util.List;

@Slf4j
@AllArgsConstructor
@Service
@Transactional(readOnly = true)
public class ItemRequestServiceImpl implements ItemRequestService {
    private final ItemRequestStorage itemRequestStorage;
    private final UserStorage userStorage;

    @Transactional
    @Override
    public ItemRequestDto addRequest(ItemRequestDto itemRequestDto, long requesterId) {
        validate(itemRequestDto);
        User requester = getUserFromStorage(requesterId);
        ItemRequest itemRequest = ItemRequestMapper.toItemRequest(itemRequestDto, requester);
        return ItemRequestMapper.toItemRequestDto(itemRequestStorage.save(itemRequest));
    }

    @Override
    public List<ItemRequestDto> getAllRequestsForRequester(long requesterId) {
        User requester = getUserFromStorage(requesterId);
        Sort sort = Sort.by(Sort.Direction.DESC, "date");
        return ItemRequestMapper.toItemRequestDtoList(itemRequestStorage.findAllByRequester(requester, sort));
    }

    @Override
    public List<ItemRequestDto> getAllRequests(long requesterId, int from, int size) {
        Sort sort = Sort.by(Sort.Direction.DESC, "date");
        int page = getPage(from, size);
        return ItemRequestMapper
                .toItemRequestDtoList(itemRequestStorage
                        .findAllByRequesterIdNotEquals(requesterId, PageRequest.of(page, size, sort)));
    }

    @Override
    public ItemRequestDto getRequest(long requestId, long userId) {
        if (!userStorage.existsById(userId)) {
            throw new NotFoundException(String.format("Пользователя с id= " + userId + " не существует"));
        }
        ItemRequest itemRequest = getRequestFromStorage(requestId);
        return ItemRequestMapper.toItemRequestDto(itemRequest);
    }

    private User getUserFromStorage(long id) {
        return userStorage.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("Пользователя с id= " + id + " не существует")));
    }

    private int getPage(int from, int size) {
        if (from < 0 || size <= 0) {
            throw new ValidateException("Неверные параметры page или size");
        } else {
            return from / size;
        }
    }

    private ItemRequest getRequestFromStorage(long id) {
        return itemRequestStorage.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("Запрос с id= " + id + " не существует")));
    }

    private void validate(ItemRequestDto itemRequestDto) {
        if (itemRequestDto.getDescription() == null) {
            throw new ValidateException("Описание запроса не может быть пустым");
        }
    }
}

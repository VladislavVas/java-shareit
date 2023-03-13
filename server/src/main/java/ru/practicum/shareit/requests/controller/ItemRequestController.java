package ru.practicum.shareit.requests.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.requests.dto.ItemRequestDto;
import ru.practicum.shareit.requests.service.ItemRequestServiceImpl;

import java.util.List;

@Slf4j
@RestController
@RequestMapping(path = "/requests")
@AllArgsConstructor
public class ItemRequestController {

    private final ItemRequestServiceImpl itemRequestService;

    @PostMapping
    public ItemRequestDto addRequest(@RequestHeader("X-Sharer-User-Id") long requesterId,
                                     @RequestBody ItemRequestDto itemRequestDto) {
        log.info("ItemRequestController: POST на добавление нового запроса");
        return itemRequestService.addRequest(itemRequestDto, requesterId);
    }

    @GetMapping("/all")
    public List<ItemRequestDto> getAllRequests(@RequestParam(value = "from", required = false, defaultValue = "0")
                                               int from,
                                               @RequestParam(value = "size", required = false, defaultValue = "20")
                                               int size,
                                               @RequestHeader("X-Sharer-User-Id") long requesterId) {
        log.info("ItemRequestController: GET всех запросов");
        return itemRequestService.getAllRequests(requesterId, from, size);
    }

    @GetMapping
    public List<ItemRequestDto> getAllRequestsForRequester(@RequestHeader("X-Sharer-User-Id") long requesterId) {
        log.info("ItemRequestController: GET всех запросов для пользователя id=" + requesterId);
        return itemRequestService.getAllRequestsForRequester(requesterId);
    }


    @GetMapping("{requestId}")
    public ItemRequestDto getRequestById(@PathVariable long requestId,
                                         @RequestHeader("X-Sharer-User-Id") long userId) {
        log.info("ItemRequestController: GET запроса id=" + requestId);
        return itemRequestService.getRequest(requestId, userId);
    }
}

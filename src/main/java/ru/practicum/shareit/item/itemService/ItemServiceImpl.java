package ru.practicum.shareit.item.itemService;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.storage.BookingStorage;
import ru.practicum.shareit.exception.AccessException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidateException;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.CommentStorage;
import ru.practicum.shareit.item.storage.ItemStorage;
import ru.practicum.shareit.requests.model.ItemRequest;
import ru.practicum.shareit.requests.storage.ItemRequestStorage;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserStorage;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Slf4j
@Service
@AllArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final UserStorage userStorage;
    private final ItemStorage itemStorage;
    private final BookingStorage bookingStorage;
    private final CommentStorage commentStorage;
    private final ItemRequestStorage itemRequestStorage;


    public List<ItemDtoForRequest> getAllItems(long userId, int from, int size) {
        log.info("ItemService: обработка запроса всех вещей пользователя id " + userId);
        User user = getUserFromStorage(userId);
        int page = getPage(from, size);
        List<ItemDtoForRequest> itemsDto = ItemMapper.toListItemDtoForRequest(itemStorage.getItemsByOwner(user, PageRequest.of(page, size, Sort.by("id"))));
        itemsDto.forEach(this::setBookingInDto);
        return itemsDto;
    }

    public ItemDtoForRequest getItem(long userId, long itemId) {
        Item item = getItemFromStorage(itemId);
        ItemDtoForRequest itemDtoForRequest = ItemMapper.toItemtDtoForRequest(item);
        addComments(List.of(itemDtoForRequest));
        log.info("ItemService: обработка запроса вещи id " + itemId + " у пользлвателя id " + userId);
        if (item.getOwner().getId() != userId) {
            return itemDtoForRequest;
        } else {
            return setBookingInDto(itemDtoForRequest);
        }
    }

    public ItemDto addNewItem(long userId, ItemDto itemDto) {
        User user = getUserFromStorage(userId);
        ItemRequest itemRequest = itemDto.getRequestId() != null ? getItemRequestFromStorage(itemDto.getRequestId()) : null;
        log.info("ItemService: обработка запроса на добавление вещи: " + itemDto.getName());
        Item item = itemStorage.save(ItemMapper.toItem(itemDto, user, itemRequest));
        return ItemMapper.toDto(item);
    }

    public ItemDto updateItem(long userId, long itemId, ItemDto itemDto) {
        log.info("ItemService: обработка запроса на обновление вещи id " + itemId);
        Item itemExisting = getItemFromStorage(itemId);
        if (itemExisting.getOwner().getId() == userId) {
            if (itemDto.getName() != null) {
                itemExisting.setName(itemDto.getName());
            }
            if (itemDto.getDescription() != null) {
                itemExisting.setDescription(itemDto.getDescription());
            }
            if (itemDto.getAvailable() != null) {
                itemExisting.setAvailable(itemDto.getAvailable());
            }
            if (itemDto.getRequestId() != null) {
                ItemRequest itemRequest = getItemRequestFromStorage(itemDto.getRequestId());
                itemExisting.setItemRequest(itemRequest);
            }
            return ItemMapper.toDto(itemStorage.save(itemExisting));
        } else {
            throw new AccessException("Только собственник может обновлять вещь");
        }
    }


    public List<ItemDto> searchItemByText(String text, int from, int size) {
        if (text.isBlank()) {
            return new ArrayList<>();
        }
        int page = getPage(from, size);
        log.info("ItemService: обработка запроса на поиск вещи по тексту: " + text); //может поломаться
        return ItemMapper.toDtoList(itemStorage.searchItem(PageRequest.of(page, size), text));
    }

    @Transactional
    public CommentDto addCommentToItem(CommentDto commentDto, long itemId, long userId) {
        Item item = getItemFromStorage(itemId);
        if (bookingStorage.isExists(itemId, userId, LocalDateTime.now())) {
            User author = getUserFromStorage(userId);
            Comment comment = CommentMapper.toComment(commentDto, item, author);
            commentDto = CommentMapper.toCommentDto(commentStorage.save(comment));
        } else {
            throw new ValidateException("Невозможно оставить комментарий");
        }
        return commentDto;
    }


    private Item getItemFromStorage(long id) {
        return itemStorage.findById(id)
                .orElseThrow(() -> new NotFoundException("Вещь с id= " + id + " не существует"));
    }

    private User getUserFromStorage(long id) {
        return userStorage.findById(id)
                .orElseThrow(() -> new NotFoundException("Пользователя с id= " + id + " не существует"));
    }

    private ItemRequest getItemRequestFromStorage(long id) {
        return itemRequestStorage.findById(id)
                .orElseThrow(() -> new NotFoundException("ItemRequest с id=" + id + " не существует"));
    }

    private ItemDtoForRequest setBookingInDto(ItemDtoForRequest itemDtoForRequest) {
        Booking lastBooking = bookingStorage.findBookingByItemWithDateBefore(itemDtoForRequest.getId(),
                LocalDateTime.now());
        Booking nextBooking = bookingStorage.findBookingByItemWithDateAfter(itemDtoForRequest.getId(),
                LocalDateTime.now());
        if (lastBooking != null) {
            itemDtoForRequest.setLastBooking(ItemMapper.setBookingToItemDto(lastBooking));
        }
        if (nextBooking != null) {
            itemDtoForRequest.setNextBooking(ItemMapper.setBookingToItemDto(nextBooking));
        }
        return itemDtoForRequest;
    }

    private void addComments(List<ItemDtoForRequest> items) {
        List<Comment> comments;
        for (ItemDtoForRequest item : items) {
            comments = commentStorage.findByItemId(item.getId());
            item.setComments(CommentMapper.toListDto(comments));
        }
    }

    private int getPage(int from, int size) {
        if (from < 0 || size <= 0) {
            throw new ValidateException("Неверные параметры page или size");
        } else {
            return from / size;
        }
    }
}

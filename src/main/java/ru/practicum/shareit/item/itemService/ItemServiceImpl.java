package ru.practicum.shareit.item.itemService;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserStorage;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@AllArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final UserStorage userStorage;
    private final ItemStorage itemStorage;
    private final BookingStorage bookingStorage;
    private final ItemMapper itemMapper;
    private final CommentStorage commentStorage;
    private final CommentMapper commentMapper;


    public List<ItemDtoForRequest> getAllItems(long userId) {
        log.info("ItemService: обработка запроса всех вещей пользователя id " + userId);
        User user = getUserFromStorage(userId);
        List<ItemDtoForRequest> itemsDto = itemMapper.toListItemRequestDto(itemStorage.getItemsByOwner(user));
        for (ItemDtoForRequest item : itemsDto) {
            setBookingInDto(item);
        }
        Collections.sort(itemsDto, (a, b) -> a.getId() < b.getId() ? -1 : a.getId() == b.getId() ? 0 : 1);
        return itemsDto;
    }

    public ItemDtoForRequest getItem(long userId, long itemId) {
        Item item = getItemFromStorage(itemId);
        ItemDtoForRequest itemDtoForRequest = itemMapper.toItemRequestDto(item);
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
        log.info("ItemService: обработка запроса на добавление вещи: " + itemDto.getName());
        Item item = itemStorage.save(itemMapper.toItem(itemDto, user));
        ItemDto dto = itemMapper.toDto(item);
        return dto;
    }

    public ItemDto updateItem(long userId, long itemId, ItemDto itemDto) {
        log.info("ItemService: обработка запроса на обновление вещи id " + itemId);
        Item itemExisting = getItemFromStorage(itemId);
        if (itemExisting.getOwner().getId() == userId){
            if (itemDto.getName() != null) {
                itemExisting.setName(itemDto.getName());
            }
            if (itemDto.getDescription() != null) {
                itemExisting.setDescription(itemDto.getDescription());
            }
            if (itemDto.getAvailable() != null) {
                itemExisting.setAvailable(itemDto.getAvailable());
            }
            return itemMapper.toDto(itemStorage.save(itemExisting));
        } else {
            throw new AccessException("Только собственник может обновлять вещь");
        }
    }

    public void deleteItem(long userId, long itemId) {
        User user = getUserFromStorage(userId);
        Item item = getItemFromStorage(itemId);
        log.info("ItemService: обработка запроса на удаление вещи id " + userId);
        itemStorage.delete(item);
    }

    public List<ItemDto> searchItemByText(String text) {
        if (text.isBlank())
        {
            return new ArrayList<>();
        }
        log.info("ItemService: обработка запроса на поиск вещи по тексту: " + text);
        return itemMapper.toDtoList(itemStorage.searchItem(text));
    }

    @Transactional
    public CommentDto addCommentToItem(CommentDto commentDto, long itemId, long userId) {
        Item item = getItemFromStorage(itemId);
        if (bookingStorage.isExists(itemId, userId, LocalDateTime.now())) {
            User author = getUserFromStorage(userId);
            Comment comment = commentMapper.toComment(commentDto, item, author);
            commentDto = commentMapper.toCommentDto(commentStorage.save(comment));
        } else {
            throw new ValidateException("Невозможно оставить комментарий");
        }
        return commentDto;
    }


    private Item getItemFromStorage(long id) {
        Optional<Item> item = itemStorage.findById(id);
        if (!item.isPresent()) {
            throw new NotFoundException("Вещь с id= " + id + " не существует");
        } else {
            return item.get();
        }
    }

    private User getUserFromStorage(long id) {
        Optional<User> user = userStorage.findById(id);
        if (!user.isPresent()) {
            throw new NotFoundException("Пользователя с id= " + id + " не существует");
        } else {
            return user.get();
        }
    }

    private ItemDtoForRequest setBookingInDto(ItemDtoForRequest itemDtoForRequest) {
        Booking lastBooking = bookingStorage.findBookingByItemWithDateBefore(itemDtoForRequest.getId(),
                LocalDateTime.now());
        Booking nextBooking = bookingStorage.findBookingByItemWithDateAfter(itemDtoForRequest.getId(),
                LocalDateTime.now());
        if (lastBooking != null) {
            itemDtoForRequest.setLastBooking(itemMapper.setBookingToItemDto(lastBooking));
        }
        if (nextBooking != null) {
            itemDtoForRequest.setNextBooking(itemMapper.setBookingToItemDto(nextBooking));
        }
        return itemDtoForRequest;
    }

    private void addComments(List<ItemDtoForRequest> items) {
        List<Comment> comments;
        for (ItemDtoForRequest item : items) {
            comments = commentStorage.findByItemId(item.getId());
            item.setComments(commentMapper.toListDto(comments));
        }
    }
}

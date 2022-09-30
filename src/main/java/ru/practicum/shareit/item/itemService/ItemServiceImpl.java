package ru.practicum.shareit.item.itemService;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.storage.ItemStorageInMemory;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.service.UserServiceImpl;

import javax.validation.ValidationException;
import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final UserServiceImpl userServiceImpl;
    private final ItemStorageInMemory itemStorage;
    private final ItemMapper mapper;

    public List<ItemDto> getAllItems(long userId) {
        log.info("ItemService: обработка запроса всех вещей пользователя id " + userId);
        return mapper.toDtoList(itemStorage.getItems(userId));
    }

    public ItemDto getItem(long userId, long itemId) {
        validate(itemId);
        log.info("ItemService: обработка запроса вещи id " + itemId + " у пользлвателя id " + userId);
        return mapper.toDto(itemStorage.getItem(itemId));
    }

    public ItemDto addNewItem(long userId, ItemDto itemDto) {
        userServiceImpl.getUser(userId);
        log.info("ItemService: обработка запроса на добавление вещи: " + itemDto.getName());
        Item item = mapper.toItem(itemDto);
        ItemDto dto = mapper.toDto(itemStorage.addItem(userId, item));
        return dto;
    }

    public ItemDto updateItem(long userId, long itemId, ItemDto itemDto) {
        Item itemExisting = itemStorage.getItem(itemId);
        log.info("ItemService: обработка запроса на обновление вещи id " + itemId);
        Item updateItem = itemStorage.updateItem(userId, itemExisting, mapper.toItem(itemDto));
        return mapper.toDto(updateItem);
    }

    public void deleteItem(long userId, long itemId) {
        validate(userId);
        validate(itemId);
        log.info("ItemService: обработка запроса на удаление вещи id " + userId);
        itemStorage.deleteItem(userId, itemId);
    }

    public List<ItemDto> searchItemByText(String text) {
        log.info("ItemService: обработка запроса на поиск вещи по тексту: " + text);
        return mapper.toDtoList(itemStorage.searchItem(text));
    }

    private void validate(long id) {
        if (id <= 0) {
            throw new ValidationException("Неверно указан id");
        }
    }
}

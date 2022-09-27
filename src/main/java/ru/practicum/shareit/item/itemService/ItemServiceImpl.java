package ru.practicum.shareit.item.itemService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.ItemStorageInMemory;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.service.UserServiceImpl;

import javax.validation.ValidationException;
import java.util.List;

@Slf4j
@Service
public class ItemServiceImpl implements ItemService {
    private final UserServiceImpl userServiceImpl;
    private final ItemStorageInMemory itemStorage;

    @Autowired
    public ItemServiceImpl(UserServiceImpl userServiceImpl, ItemStorageInMemory itemStorage) {
        this.userServiceImpl = userServiceImpl;
        this.itemStorage = itemStorage;
    }

    public List<ItemDto> getAllItems(long userId) {
        log.info("ItemService: обработка запроса всех вещей пользователя id " + userId);
        return itemStorage.getItems(userId);
    }

    public ItemDto getItem(long userId, long itemId) {
        validate(itemId);
        log.info("ItemService: обработка запроса вещи id " + itemId + " у пользлвателя id " + userId);
        return itemStorage.getItem(itemId);
    }

    public ItemDto addNewItem(long userId, ItemDto itemDto) {
        userServiceImpl.getUser(userId);
        log.info("ItemService: обработка запроса на добавление вещи: " + itemDto.getName());
        return itemStorage.addItem(userId, itemDto);
    }

    public ItemDto updateItem(long userId, long itemId, ItemDto itemDto) {
        ItemDto itemDtoExisting = itemStorage.getItem(itemId);
        log.info("ItemService: обработка запроса на обновление вещи id " + itemId);
        return itemStorage.updateItem(userId, itemDtoExisting, itemDto);
    }

    public void deleteItem(long userId, long itemId) {
        validate(userId);
        validate(itemId);
        log.info("ItemService: обработка запроса на удаление вещи id " + userId);
        itemStorage.deleteItem(userId, itemId);
    }

    public List<ItemDto> searchItemByText(String text) {
        log.info("ItemService: обработка запроса на поиск вещи по тексту: " + text);
        return itemStorage.searchItem(text);
    }

    private void validate(long id) {
        if (id <= 0) {
            throw new ValidationException("Неверно указан id");
        }
    }
}

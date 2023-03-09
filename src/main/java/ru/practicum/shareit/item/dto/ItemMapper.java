package ru.practicum.shareit.item.dto;

import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.requests.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor
@Component
public class ItemMapper {
    public static Item toItem(ItemDto dto, @Nullable User user, @Nullable ItemRequest itemRequest) {
        return Item.builder()
                .id(dto.getId())
                .name(dto.getName())
                .description(dto.getDescription())
                .available(dto.getAvailable())
                .owner(user)
                .itemRequest(itemRequest)
                .build();
    }

    public static ItemDto toDto(Item item) {
        return ItemDto.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .requestId(item.getItemRequest() != null ? item.getItemRequest().getId() : null)
                .build();
    }


    public static List<ItemDto> toDtoList(List<Item> itemList) {
        return itemList.stream()
                .map(ItemMapper::toDto)
                .collect(Collectors.toList());
    }

    public static ItemDtoForRequest toItemtDtoForRequest(Item item) {
        return ItemDtoForRequest.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .requestId(item.getItemRequest() != null ? item.getItemRequest().getId() : null)
                .build();
    }

    public static List<ItemDtoForRequest> toListItemDtoForRequest(List<Item> items) {
        return items.stream()
                .map(ItemMapper::toItemtDtoForRequest)
                .collect(Collectors.toList());
    }

    public static ItemDtoForRequest.BookingDto setBookingToItemDto(Booking booking) {
        return new ItemDtoForRequest.BookingDto(booking.getId(),
                booking.getStart(),
                booking.getEnd(),
                booking.getBooker().getId());
    }

}

package ru.practicum.shareit.booking.bookingService;


import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.dto.BookItemRequestDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.storage.BookingStorage;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidateException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemStorage;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserStorage;

import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class BookingServiceImpl implements BookingService {

    private final BookingStorage bookingStorage;
    private final UserStorage userStorage;
    private final ItemStorage itemStorage;

    @Transactional
    @Override
    public BookingResponseDto addBooking(BookItemRequestDto bookItemRequestDto, long userId) {
        Item item = getItemFromStorage(bookItemRequestDto.getItemId());
        User user = getUserFromStorage(userId);
        if (validate(bookItemRequestDto, item, user)) {
            Booking booking = BookingMapper.toBooking(bookItemRequestDto, item, user);
            booking.setStatus(Status.WAITING);
            Booking result = bookingStorage.save(booking);
            return BookingMapper.toBookingResponseDto(result);
        } else {
            throw new ValidateException("Ошибка валидации. Проверьте дату, доступность и владельца вещи");
        }
    }

    @Transactional
    @Override
    public BookingResponseDto approveBooking(long bookingId, Boolean approved, long userId) {
        Booking booking = checkApprove(bookingId, approved, userId);
        return BookingMapper.toBookingResponseDto(bookingStorage.save(booking));
    }

    @Transactional(readOnly = true)
    @Override
    public List<BookingResponseDto> getSortedListBookingByUserId(long bookerId, State state, int from, int size) {
        if (userStorage.existsById(bookerId)) {
            List<Booking> bookings;
            int page = getPage(from, size);
            Sort sort = Sort.by(Sort.Direction.DESC, "start");
            switch (state) {
                case ALL:
                    bookings = bookingStorage.findBookingByBookerId(bookerId,
                            PageRequest.of(page, size, sort));
                    break;
                case CURRENT:
                    bookings = bookingStorage.findBookingsCurrentForBooker(bookerId, LocalDateTime.now(),
                            PageRequest.of(page, size, sort));
                    break;
                case PAST:
                    bookings = bookingStorage.findBookingsPastForBooker(bookerId, LocalDateTime.now(),
                            PageRequest.of(page, size, sort));
                    break;
                case FUTURE:
                    bookings = bookingStorage.findBookingsFutureForBooker(bookerId, LocalDateTime.now(),
                            PageRequest.of(page, size, sort));
                    break;
                case WAITING:
                    bookings = bookingStorage.findBookingsByStatusAndBookerId(bookerId, Status.WAITING,
                            PageRequest.of(page, size, sort));
                    break;
                case REJECTED:
                    bookings = bookingStorage.findBookingsByStatusAndBookerId(bookerId, Status.REJECTED,
                            PageRequest.of(page, size, sort));
                    break;
                default:
                    throw new ValidateException("Unknown state: UNSUPPORTED_STATUS");
            }
            return BookingMapper.toListBookingDto(bookings);
        } else {
            throw new NotFoundException(("Пользователя с id= " + bookerId + " не существует"));
        }
    }


    @Override
    public BookingResponseDto getBookingByUserId(long bookingId, long userId) {
        if (!userStorage.existsById(userId)) {
            throw new NotFoundException("Пользователя с id= " + userId + " не найден");
        }
        if (!bookingStorage.existsById(bookingId)) {
            throw new NotFoundException("Бронирования с id= " + bookingId + " не существует");
        }
        Booking booking = getBookingFromStorage(bookingId);
        if (booking.getBooker().getId() == userId || booking.getItem().getOwner().getId() == userId) {
            return BookingMapper.toBookingResponseDto(booking);
        } else {
            throw new NotFoundException("Бронирование не найдено");
        }
    }

    @Override
    public List<BookingResponseDto> getListBookingByOwnerId(long userId, State state, int from, int size) {
        if (!userStorage.existsById(userId)) {
            throw new NotFoundException("Пользователя с id= " + userId + " не существует");
        }
        getPage(from,size);
        List<Booking> bookings;
        Sort sort = Sort.by(Sort.Direction.DESC, "start");
        switch (state) {
            case ALL:
                bookings = bookingStorage.findAllBookingsForItemOwner(userId,
                        PageRequest.of(from, size, sort));
                break;
            case CURRENT:
                bookings = bookingStorage.findBookingsCurrentForItemOwner(userId, LocalDateTime.now(),
                        PageRequest.of(from, size, sort));
                break;
            case PAST:
                bookings = bookingStorage.findBookingsPastForItemOwner(userId, LocalDateTime.now(),
                        PageRequest.of(from, size, sort));
                break;
            case FUTURE:
                bookings = bookingStorage.findBookingsFutureForItemOwner(userId, LocalDateTime.now(),
                        PageRequest.of(from, size, sort));
                break;
            case WAITING:
                bookings = bookingStorage.findBookingsByStatusForItemOwner(userId, Status.WAITING,
                        PageRequest.of(from, size, sort));
                break;
            case REJECTED:
                bookings = bookingStorage.findBookingsByStatusForItemOwner(userId, Status.REJECTED,
                        PageRequest.of(from, size, sort));
                break;
            default:
                throw new ValidateException("Unknown state: UNSUPPORTED_STATUS");
        }
        return BookingMapper.toListBookingDto(bookings);
    }

    private User getUserFromStorage(long userId) {
        return userStorage.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователя с id= " + userId + " не существует"));
    }

    private Item getItemFromStorage(long itemId) {
        return itemStorage.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Вещь с id= " + itemId + " не существует"));
    }

    private Booking getBookingFromStorage(long id) {
        return bookingStorage.findById(id)
                .orElseThrow(() -> new NotFoundException("Бронирование с id= " + id + " не существует"));
    }

    private boolean validate(BookItemRequestDto bookItemRequestDto, Item item, User booker) {
        LocalDateTime start = bookItemRequestDto.getStart();
        LocalDateTime end = bookItemRequestDto.getEnd();
        if (end.isBefore(start) && !start.isEqual(end)) {
            throw new ValidateException("Дата окончания бронирования не может быть раньше даты начала бронирования");
        } else if (item.getAvailable().equals(false)) {
            throw new ValidateException(String.format("Вещь id = " + item.getId() + " недоступна"));
        } else if (item.getOwner().getId() == booker.getId()) {
            throw new NotFoundException("Собственник не может забронировать свою вещь");
        } else {
            return true;
        }
    }

    private Booking checkApprove(long bookingId, Boolean approved, long userId) {
        Booking booking = getBookingFromStorage(bookingId);
        Item item;
        item = booking.getItem();
        if (item.getOwner().getId() != userId) {
            throw new NotFoundException("Пользователя с id= " + userId + " не существует");
        } else if (item.getAvailable().equals(false)) {
            throw new ValidateException("Вещь id = " + item.getId() + " недоступна");
        }
        if (booking.getStatus() == Status.APPROVED) {
            throw new ValidateException("Бронирование уже подтвержденно");
        }
        booking.setStatus(approved ? Status.APPROVED : Status.REJECTED);
        return booking;
    }

    private int getPage(int from, int size) {
        if (from < 0 || size <= 0) {
            throw new ValidateException("Неверные параметры page или size");
        } else {
            return from / size;
        }
    }
}

package ru.practicum.shareit.booking.bookingService;


import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
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
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@AllArgsConstructor
@Slf4j
public class BookingServiceImpl implements BookingService {

    private final BookingStorage bookingStorage;
    private final UserStorage userStorage;
    private final ItemStorage itemStorage;

    @Transactional
    @Override
    public BookingResponseDto addBooking(BookingRequestDto bookingRequestDto, long userId) {
        Item item = getItemFromStorage(bookingRequestDto.getItemId());
        User user = getUserFromStorage(userId);
        if (validate(bookingRequestDto, item, user)){
            Booking booking = BookingMapper.toBooking(bookingRequestDto, item, user);
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
    public List<BookingResponseDto> getSortedListBookingByUserId(long bookerId, State state) {
        User user = getUserFromStorage(bookerId);
        List<Booking> bookings;
        Sort sort = Sort.by(Sort.Direction.DESC, "start");
        switch (state) {
            case ALL:
                bookings = bookingStorage.findBookingByBookerIdOrderByStartDesc(bookerId);
                break;
            case CURRENT:
                bookings = bookingStorage.findBookingsCurrentForBooker(bookerId, LocalDateTime.now(), sort);
                break;
            case PAST:
                bookings = bookingStorage.findBookingsPastForBooker(bookerId, LocalDateTime.now(), sort);
                break;
            case FUTURE:
                bookings = bookingStorage.findBookingsFutureForBooker(bookerId, LocalDateTime.now(), sort);
                break;
            case WAITING:
                bookings = bookingStorage.findBookingsByStatusAndBookerId(bookerId, Status.WAITING);
                break;
            case REJECTED:
                bookings = bookingStorage.findBookingsByStatusAndBookerId(bookerId, Status.REJECTED);
                break;
            default:
                throw new ValidateException("Unknown state: UNSUPPORTED_STATUS");
        }
        return BookingMapper.toListBookingDto(bookings);
    }


    @Override
    public BookingResponseDto getBookingByUserId(long bookingId, long userId) {
        getUserFromStorage(userId);
        getBookingFromStorage(bookingId);
        if (userStorage.findById(userId).isEmpty()) {
            throw new NotFoundException("Пользователя с id= " + userId +
                    " или бронирования " + bookingId +" не существует");
        } else {
            Booking booking = getBookingFromStorage(bookingId);
            if (booking.getBooker().getId() == userId || booking.getItem().getOwner().getId() == userId) {
                return BookingMapper.toBookingResponseDto(booking);
            } else {
                throw new NotFoundException("Пользователя с id= " + userId + " не существует");
            }
        }
    }

    @Override
    public List<BookingResponseDto> getListBookingByOwnerId(long userId, State state) {
        if (!userStorage.existsById(userId)) {
            throw new NotFoundException("Пользователя с id= " + userId + " не существует");
        }
        List<Booking> bookings;
        Sort sort = Sort.by(Sort.Direction.DESC, "start");
        switch (state) {
            case ALL:
                bookings = bookingStorage.findAllBookingsForItemOwner(userId, sort);
                break;
            case CURRENT:
                bookings = bookingStorage.findBookingsCurrentForItemOwner(userId, LocalDateTime.now(), sort);
                break;
            case PAST:
                bookings = bookingStorage.findBookingsPastForItemOwner(userId, LocalDateTime.now(), sort);
                break;
            case FUTURE:
                bookings = bookingStorage.findBookingsFutureForItemOwner(userId, LocalDateTime.now(), sort);
                break;
            case WAITING:
                bookings = bookingStorage.findBookingsByStatusForItemOwner(userId, Status.WAITING);
                break;
            case REJECTED:
                bookings = bookingStorage.findBookingsByStatusForItemOwner(userId, Status.REJECTED);
                break;
            default:
                throw new ValidateException("Unknown state: UNSUPPORTED_STATUS");
        }
        return BookingMapper.toListBookingDto(bookings);
    }

    private User getUserFromStorage(long userId) {
        Optional<User> user = userStorage.findById(userId);
        if (user.isEmpty()) {
            throw new NotFoundException("Пользователя с id= " + userId + " не существует");
        } else {
            return user.get();
        }
    }

    private Item getItemFromStorage(long itemId) {
        Optional<Item> item = itemStorage.findById(itemId);
        if (item.isEmpty()) {
            throw new NotFoundException("Вещь с id= " + itemId + " не существует");
        } else {
            return item.get();
        }
    }

    private Booking getBookingFromStorage(long id) {
        Optional<Booking> booking = bookingStorage.findById(id);
        if (booking.isEmpty()) {
            throw new NotFoundException("Бронирование с id= " + id + " не существует");
        } else {
            return booking.get();
        }
    }

    private boolean validate(BookingRequestDto bookingRequestDto, Item item, User booker) {
        LocalDateTime start = bookingRequestDto.getStart();
        LocalDateTime end = bookingRequestDto.getEnd();
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
}

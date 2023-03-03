package ru.practicum.shareit.booking.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.bookingService.BookingServiceImpl;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.model.Status;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BookingController.class)
class BookingControllerTest {

    @MockBean
    private BookingServiceImpl bookingService;
    @Autowired
    private ObjectMapper mapper;
    @Autowired
    private MockMvc mvc;

    private BookingResponseDto bookingResponseDto;
    private BookingRequestDto bookingRequestDto;

    @BeforeEach
    void getEntities() {
        bookingRequestDto = BookingRequestDto.builder()
                .start(LocalDateTime.of(2025, 01, 01, 01, 01))
                .end(LocalDateTime.of(2025, 02, 01, 01, 01))
                .itemId(1L)
                .build();

        bookingResponseDto = BookingResponseDto.builder()
                .id(1l)
                .start(LocalDateTime.of(2025, 01, 01, 01, 01))
                .end(LocalDateTime.of(2025, 02, 01, 01, 01))
                .booker(null)
                .item(null)
                .status(Status.WAITING)
                .build();
    }

    @Test
    void addBooking() throws Exception {
        when(bookingService.addBooking(bookingRequestDto, 1L)).thenReturn(bookingResponseDto);
        mvc.perform(post("/bookings")
                        .content(mapper.writeValueAsString(bookingRequestDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.start").value("2025-01-01T01:01:00"))
                .andExpect(jsonPath("$.end").value("2025-02-01T01:01:00"))
                .andExpect(jsonPath("$.status").value(Status.WAITING.toString()));
    }

    @Test
    void approveBooking() throws Exception {
        bookingResponseDto.setStatus(Status.APPROVED);
        when(bookingService.approveBooking(1L, true, 1L)).thenReturn(bookingResponseDto);
        mvc.perform(patch("/bookings/{bookingId}", 1L)
                        .param("approved", "true")
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.start").value("2025-01-01T01:01:00"))
                .andExpect(jsonPath("$.end").value("2025-02-01T01:01:00"))
                .andExpect(jsonPath("$.status").value(Status.APPROVED.toString()));
    }

    @Test
    void getBooking() throws Exception {
        when(bookingService.getBookingByUserId(1L, 1L)).thenReturn(bookingResponseDto);
        mvc.perform(get("/bookings/{bookingId}", 1L)
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.start").value("2025-01-01T01:01:00"))
                .andExpect(jsonPath("$.end").value("2025-02-01T01:01:00"))
                .andExpect(jsonPath("$.status").value(Status.WAITING.toString()));
    }

    @Test
    void getListBookingByOwnerId() throws Exception {
        List<BookingResponseDto> bookingDtoList = List.of(bookingResponseDto);
        when(bookingService.getListBookingByOwnerId(anyLong(), any(), anyInt(), anyInt())).thenReturn(bookingDtoList);
        mvc.perform(get("/bookings/owner")
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(bookingDtoList.size()));
    }

    @Test
    void getSortedListBookingByUserId() throws Exception {
        List<BookingResponseDto> bookingDtoList = List.of(bookingResponseDto);
        when(bookingService.getSortedListBookingByUserId(anyLong(), any(), anyInt(), anyInt())).thenReturn(bookingDtoList);
        mvc.perform(get("/bookings")
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(bookingDtoList.size()));
    }
}
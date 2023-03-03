package ru.practicum.shareit.requests.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.requests.dto.ItemRequestDto;
import ru.practicum.shareit.requests.service.ItemRequestServiceImpl;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ItemRequestController.class)
class ItemRequestControllerTest {
    @MockBean
    private ItemRequestServiceImpl requestService;
    @Autowired
    private ObjectMapper mapper;
    @Autowired
    private MockMvc mvc;
    private ItemRequestDto itemRequest;
    private List<ItemRequestDto> requestsList;

    @BeforeEach
    void getEntities() {
        itemRequest = ItemRequestDto.builder()
                .id(1L)
                .description("description")
                .requesterId(1L)
                .items(Collections.emptyList())
                .build();
    }

    @Test
    void addRequest() throws Exception {
        when(requestService.addRequest(any(), anyLong())).thenReturn(itemRequest);
        mvc.perform(post("/requests")
                        .content(mapper.writeValueAsString(itemRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.description").value("description"))
                .andExpect(jsonPath("$.requesterId").value(1));
    }

    @Test
    void getAllRequests() throws Exception {
        requestsList = List.of(itemRequest);
        when(requestService.getAllRequests(anyLong(), anyInt(), anyInt())).thenReturn(requestsList);
        mvc.perform(get("/requests/all")
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1));
    }

    @Test
    void getAllRequestsForRequester() throws Exception {
        requestsList = List.of(itemRequest);
        when(requestService.getAllRequestsForRequester(anyLong())).thenReturn(requestsList);
        mvc.perform(                 get("/requests")
                                .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1));
    }

    @Test
    void getRequestById() throws Exception {
        when(requestService.getRequest(anyLong(), anyLong())).thenReturn(itemRequest);
        mvc.perform(get("/requests/{requestId}", 1L)
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.description").value("description"))
                .andExpect(jsonPath("$.requesterId").value(1));
    }
}
package ru.practicum.shareit.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

/**
 * // TODO .
 */
@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class User {
    private long id;
    private String email;
    private String name;
}

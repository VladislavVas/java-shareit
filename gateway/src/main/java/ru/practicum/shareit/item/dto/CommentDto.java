package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.booking.dto.Create;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommentDto {
    private long id;
    @NotBlank(groups = Create.class, message = "Текст комментария не может быть пустым")
    private String text;
    private String authorName;
    private LocalDateTime dateCreate;
}

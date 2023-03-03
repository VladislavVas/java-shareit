package ru.practicum.shareit.item.storage;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;

import java.util.Collection;
import java.util.List;

public interface CommentStorage extends JpaRepository<Comment, Long> {
    @Query("from Comment c where c.item.id = :itemId")
    List<Comment> findByItemId(@Param("itemId") long itemId);

    List<Comment> findByItemIn(Collection<Item> item, Sort by);
}

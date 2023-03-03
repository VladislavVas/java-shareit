package ru.practicum.shareit.requests.storage;


import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.requests.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.util.List;

@Repository
public interface ItemRequestStorage extends JpaRepository<ItemRequest, Long> {
    @Query(value = "from ItemRequest where requester.id <> :requesterId")
    List<ItemRequest> findAllByRequesterIdNotEquals(@Param("requesterId") long requester, Pageable pageable);
    List<ItemRequest> findAllByRequester(User requester, Sort sort);
}

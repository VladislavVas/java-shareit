package ru.practicum.shareit.requests.model;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name = "requests")
public class ItemRequest {

    @Id
    @Column(name = "request_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = "request_description")
    private String description;
    @ManyToOne
    @JoinColumn(name = "requester_id", referencedColumnName = "user_id")
    private User requester;
    @Column(name = "date")
    @DateTimeFormat(pattern = "yyyy-MM-ddTHH:mm:ss")
    private LocalDateTime date;
    @OneToMany(mappedBy = "itemRequest")
    private List<Item> items;

    public ItemRequest(String description, User requester, LocalDateTime date, List<Item> items) {
        this.description = description;
        this.requester = requester;
        this.date = date;
        this.items = items;
    }

    @PrePersist
    protected void items() {
        this.items = new ArrayList<>();
    }
}

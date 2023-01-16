package ru.practicum.shareit.user.model;

import lombok.*;

import javax.persistence.*;

@Data
@AllArgsConstructor
@Builder
@Table(name = "users")
@Entity
@ToString
@EqualsAndHashCode
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    @EqualsAndHashCode.Exclude
    private long id;

    @Column(name = "user_name")
    private String name;

    @Column(name = "user_email")
    private String email;
}

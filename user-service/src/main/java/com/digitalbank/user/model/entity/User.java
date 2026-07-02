package com.digitalbank.user.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @Column(unique = true)
    private String email;
    private String password;
    @Enumerated(EnumType.STRING)
    private Role role = Role.USER;
    @Enumerated(EnumType.STRING)
    private Status status = Status.NEW;

    public User (String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }
}
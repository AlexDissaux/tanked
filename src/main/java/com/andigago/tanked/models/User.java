package com.andigago.tanked.models;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by stella on 10/12/18.
 */

@Entity
@Data
public class User {

    @Id
    @GeneratedValue
    private Long id;

    @NotNull
    @Column(unique = true)
    private String username;

    @NotNull
    @Getter(AccessLevel.NONE)
    private String password;

    @Column(name = "points", nullable = false, columnDefinition = "int default 0")
    private int points;

    @OneToMany(
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    @JoinColumn(name = "owner_id")
    private List<Map> maps = new ArrayList<>();

    public User() {
        // for hibernate
    }

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public void increasePoints() {
        points++;
    }
}
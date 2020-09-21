package com.andigago.tanked.models;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by stella on 10/12/18.
 */

@Entity
@Data
public class Map {


    @Id
    @GeneratedValue
    @Getter
    private int id;

    @Getter
    @NotNull
    private String name;

    @OneToMany(
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    @LazyCollection(LazyCollectionOption.FALSE)
    @JoinColumn(name = "map_id")
    private List<Rectangle> rectangles = new ArrayList<>();

    @OneToMany(
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    @LazyCollection(LazyCollectionOption.FALSE)
    @JoinColumn(name = "map_id")
    private List<Line> lines = new ArrayList<>();

    @OneToMany(
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    @LazyCollection(LazyCollectionOption.FALSE)
    @JoinColumn(name = "map_id")
    private List<Circle> circles = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @Getter(AccessLevel.NONE)
    private User owner;

    public Map() {
        // for hibernate
    }
}
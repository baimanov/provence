package com.example.provence.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Setter
@Getter
@Entity
@RequiredArgsConstructor
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;
    private String picture;
    private double price;
    private boolean stopMenu;
    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

}

package com.einoorish.exhibitionsbackend.model.category;

import com.einoorish.exhibitionsbackend.model.exhibit.Exhibit;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "categories")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String description;

    private String thumbnail;

    @Transient
    private String thumbnailUrl;

    @Column(name = "user_id")
    private Long userId;

    @ManyToMany()
    @JoinTable(name = "category_exhibit",
            joinColumns = @JoinColumn(name = "category_id"),
            inverseJoinColumns = @JoinColumn(name = "exhibit_id"))
    private List<Exhibit> objects;

    public Category(String title, String description, String thumbnail, Long userId) {
        this.title = title;
        this.description = description;
        this.thumbnail = thumbnail;
        this.userId = userId;
    }
}
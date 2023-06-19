package com.einoorish.exhibitionsbackend.model.category;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "category_exhibit")
public class CategoryExhibit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "exhibit_id")
    private Long exhibitId;

    @Column(name = "category_id")
    private Long categoryId;

    public CategoryExhibit(Long exhibitId, Long categoryId) {
        this.exhibitId = exhibitId;
        this.categoryId = categoryId;
    }
}
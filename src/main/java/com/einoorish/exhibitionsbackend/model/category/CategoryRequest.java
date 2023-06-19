package com.einoorish.exhibitionsbackend.model.category;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CategoryRequest {
    private Long id;

    private String title;

    private String description;

    private MultipartFile thumbnail;

    private List<Long> selectedItems;
}

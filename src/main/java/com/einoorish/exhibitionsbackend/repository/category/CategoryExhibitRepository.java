package com.einoorish.exhibitionsbackend.repository.category;

import com.einoorish.exhibitionsbackend.model.category.CategoryExhibit;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.List;

@Repository
public interface CategoryExhibitRepository extends CrudRepository<CategoryExhibit, Long> {
    List<CategoryExhibit> findAllByCategoryId(Long id);
}

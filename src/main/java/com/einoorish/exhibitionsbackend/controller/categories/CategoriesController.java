package com.einoorish.exhibitionsbackend.controller.categories;

import com.einoorish.exhibitionsbackend.model.category.Category;
import com.einoorish.exhibitionsbackend.model.category.CategoryRequest;
import com.einoorish.exhibitionsbackend.model.exhibit.Exhibit;
import com.einoorish.exhibitionsbackend.repository.category.CategoryExhibitRepository;
import com.einoorish.exhibitionsbackend.service.CategoryService;
import com.einoorish.exhibitionsbackend.service.ExhibitService;
import com.einoorish.exhibitionsbackend.service.S3Service;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class CategoriesController {

    @Autowired
    CategoryService categoriesService;

    @Autowired
    CategoryExhibitRepository categoryExhibitRepository;

    @Autowired
    ExhibitService exhibitService;

    @Autowired
    S3Service s3Service;

    @Autowired
    public CategoriesController(CategoryService categoriesService, ExhibitService exhibitService) {
        this.categoriesService = categoriesService;
        this.exhibitService = exhibitService;
    }

    @GetMapping("/categories")
    @PreAuthorize("hasAuthority('PUBLISHER')")
    public String showCategoryGallery(Model model) {
        List<Category> categories = categoriesService.getCategoriesForCurrentUser();

        List<List<String>> exhibit_names = categories.stream().map(it ->
                it.getObjects().stream().map(Exhibit::getTitle).collect(Collectors.toList())).collect(Collectors.toList());

        model.addAttribute("categories", categories);

        List<String> exhibit_strings = exhibit_names.stream().map(
                it -> "This collection includes: " + it.stream().map(it2 -> "\""+it2+"\"")
                        .collect(Collectors.joining(", ")))
                .collect(Collectors.toList());

        model.addAttribute("exhibit_names", exhibit_strings);


        return "publisher/categories";
    }

    @GetMapping("/add-category")
    @PreAuthorize("hasAuthority('PUBLISHER')")
    public String addCategoryForm(Model model) {
        model.addAttribute("exhibits", exhibitService.getAllForCurrentUser());
        model.addAttribute("category", new CategoryRequest());
        return "publisher/add_category";
    }

    @GetMapping("/edit-category/{id}")
    @PreAuthorize("hasAuthority('PUBLISHER')")
    public String addCategoryForm(@PathVariable("id") Long id, Model model) {
        model.addAttribute("exhibits", exhibitService.getAllForCurrentUser());
        Category category = categoriesService.getById(id);
        CategoryRequest request = new CategoryRequest(category.getId(), category.getTitle(), category.getDescription(), null, category.getObjects().stream().map(Exhibit::getId).collect(Collectors.toList()));
        model.addAttribute("category", request);
        return "publisher/edit_category";
    }


    @PostMapping("/edit-category/{id}")
    @PreAuthorize("hasAuthority('PUBLISHER')")
    public String editCategory(CategoryRequest category) throws IOException {
        categoriesService.save(category);
        return "redirect:/categories";
    }

    @GetMapping("/delete-category/{id}")
    @PreAuthorize("hasAuthority('PUBLISHER')")
    public String deleteCategory(@PathVariable Long id) {
        categoriesService.delete(id);
        return "redirect:/categories";
    }

    @PostMapping("/add-category")
    @PreAuthorize("hasAuthority('PUBLISHER')")
    public String addCategory(CategoryRequest category) throws IOException {
        categoriesService.save(category);
        return "redirect:/categories";
    }

    @GetMapping("/category/{id}")
    @PreAuthorize("hasAuthority('PUBLISHER')")
    public String viewObjectRequestDetails(@PathVariable("id") Long id, Model model){
        Category category = categoriesService.getById(id);
        category.setThumbnailUrl(s3Service.getUrlForFileName(category.getThumbnail()));
        category.getObjects().forEach(it -> {
                    it.setThumbnailUrl(s3Service.getUrlForFileName(it.getThumbnail()));
                }
        );
        model.addAttribute("category", category);

        return "publisher/category_details";
    }

}

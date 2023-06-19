package com.einoorish.exhibitionsbackend.service;

import com.einoorish.exhibitionsbackend.model.category.Category;
import com.einoorish.exhibitionsbackend.model.category.CategoryExhibit;
import com.einoorish.exhibitionsbackend.model.category.CategoryRequest;
import com.einoorish.exhibitionsbackend.model.exhibit.Exhibit;
import com.einoorish.exhibitionsbackend.model.user.User;
import com.einoorish.exhibitionsbackend.repository.category.CategoryRepository;
import com.einoorish.exhibitionsbackend.repository.category.CategoryExhibitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryService {

    @Autowired
    private final CategoryRepository categoryRepository;

    @Autowired
    private final CategoryExhibitRepository categoryExhibitRepository;

    @Autowired
    private final ExhibitService exhibitService;

    @Autowired
    private final UserService userService;

    @Autowired
    private final S3Service s3Service;

    @Autowired
    private OrganizationsService organizationsService;

    CategoryService(CategoryRepository categoryRepository, CategoryExhibitRepository categoryExhibitRepository, ExhibitService exhibitService, UserService userService, S3Service s3Service){
        this.categoryRepository = categoryRepository;
        this.categoryExhibitRepository = categoryExhibitRepository;
        this.exhibitService = exhibitService;
        this.userService = userService;
        this.s3Service = s3Service;
    }

    public List<Category> getCategoriesForCurrentUser(){
        s3Service.createRulesForAccess();
        return getCategoriesForOrganization(userService.getCurrentUser().getId());
    }

    public Category getById(long id){
        return categoryRepository.findById(id).orElse(null);
    }

    public void save(CategoryRequest data) throws IOException {
        if(data.getId() != null) {
            // updating existing exhibit
            categoryRepository.findById(data.getId())
                    .ifPresent(category -> s3Service.deleteFile(category.getThumbnail()));
        }

        String thumbnailUrl = s3Service.saveFile(data.getThumbnail());
        List<Exhibit> exhibits = data.getSelectedItems().stream().map(exhibitService::getById).collect(Collectors.toList());

        Category category = new Category(
                data.getTitle(),
                data.getDescription(),
                thumbnailUrl,
                userService.getCurrentUser().getId());

        if(data.getId() != null) {
            category.setId(data.getId());
        }

        categoryRepository.save(category);

        exhibits.forEach(it ->
                categoryExhibitRepository.save(new CategoryExhibit(it.getId(), category.getId()))
        );
    }

    public List<Exhibit> getExhibitsInAllCategories(){
        List<Category> categories = categoryRepository.findAll();
        List<Exhibit> exhibits = categories.stream()
                .map(Category::getObjects)
                .flatMap(List::stream)
                .sorted(Comparator.comparingLong(Exhibit::getId).reversed())
                .collect(Collectors.toList());

        exhibits.forEach(it -> {
                    User user = userService.getById(it.getUserId());
                    it.setOrganization(organizationsService.findById(it.getUserId()).getOrganizationName());
                    it.setThumbnailUrl(s3Service.getUrlForFileName(it.getThumbnail(), user.getUsername()));
                    it.setFileUrl(s3Service.getUrlForFileName(it.getFile(), user.getUsername()));
                }
        );

        return exhibits;
    }

    public List<Category> getCategoriesForOrganization(Long id) {
        List<Category> categories = categoryRepository.findByUserId(id);
        User user = userService.getById(id);
        categories.forEach(it -> {
                    it.setThumbnailUrl(s3Service.getUrlForFileName(it.getThumbnail(), user.getUsername()));
                }
        );
        return categories;
    }

    public void delete(Long id) {
        Category category = categoryRepository.findById(id).orElse(null);
        if (category != null) {
            s3Service.deleteFile(category.getThumbnail());
            categoryRepository.deleteById(id);
        }
    }

    public Category getCategoryById(Long id) {
        Category category = categoryRepository.findById(id).orElse(null);
        if(category != null) {
            User user = userService.getById(category.getUserId());
            category.setThumbnailUrl(s3Service.getUrlForFileName(category.getThumbnail(), user.getUsername()));

            category.getObjects().forEach(it -> {
                        it.setThumbnailUrl(s3Service.getUrlForFileName(it.getThumbnail(), user.getUsername()));
                        it.setFileUrl(s3Service.getUrlForFileName(it.getFile(), user.getUsername()));
                    }
            );
        }
        return category;
    }
}

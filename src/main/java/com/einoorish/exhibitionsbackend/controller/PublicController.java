package com.einoorish.exhibitionsbackend.controller;

import com.einoorish.exhibitionsbackend.model.Organization;
import com.einoorish.exhibitionsbackend.model.category.Category;
import com.einoorish.exhibitionsbackend.model.exhibit.Exhibit;
import com.einoorish.exhibitionsbackend.service.CategoryService;
import com.einoorish.exhibitionsbackend.service.ExhibitService;
import com.einoorish.exhibitionsbackend.service.OrganizationsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Controller
public class PublicController {

    @Autowired
    OrganizationsService organizationsService;

    @Autowired
    ExhibitService exhibitService;

    @Autowired
    CategoryService categoryService;


    @GetMapping("/get_publication/{id}")
    public ResponseEntity<Exhibit> getPublicationById(@PathVariable Long id) {
        Exhibit publication = exhibitService.getById(id);
        if (publication == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(publication);
    }

    @GetMapping("/get_all_publications")
    public ResponseEntity<List<Exhibit>> getAllPublications() {
        List<Exhibit> publications = categoryService.getExhibitsInAllCategories();
        if (publications.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(publications);
    }

    @GetMapping("/get_organization/{id}")
    public ResponseEntity<Organization> getOrganizationById(@PathVariable Long id) {
        Organization organization = organizationsService.findById(id);
        if (organization == null) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(organization);
    }

    @GetMapping("/get_organizations")
    public ResponseEntity<List<Organization>> getOrganizations() {
        List<Organization> organizations = organizationsService.findAll();
        if (organizations.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(organizations);
    }

    @GetMapping("/get_organization_publications/{id}")
    public ResponseEntity<List<Exhibit>> getPublicationsByAuthorId(@PathVariable Long id) {
        List<Exhibit> publications = categoryService.getExhibitsInAllCategories().stream()
                .filter(exhibit -> Objects.equals(exhibit.getUserId(), id)).collect(Collectors.toList());
        if (publications.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(publications);
    }

    @GetMapping("/get_organization_categories/{id}")
    public ResponseEntity<List<Category>> getCategoriesByAuthorId(@PathVariable Long id) {
        List<Category> categories = categoryService.getCategoriesForOrganization(id);
        if (categories.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(categories);
    }

    @GetMapping("/get_category/{id}")
    public ResponseEntity<Category> getCategoriesById(@PathVariable Long id) {
        Category category = categoryService.getCategoryById(id);
        if (category == null) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(category);
    }

    @GetMapping("/get_publications")
    public ResponseEntity<List<Exhibit>> getPublicationsByFilter(
            @RequestParam(name = "subject", required = false) String subject,
            @RequestParam(name = "type", required = false) String type,
            @RequestParam(name = "text", required = false) String text,
            @RequestParam(name = "epoch", required = false) String epoch,
            @RequestParam(name = "media_type", required = false) String mediaType) {
        List<Exhibit> publications = categoryService.getExhibitsInAllCategories().stream()
                .filter( it -> {
                        if(subject!= null && !subject.isEmpty()){
                            return Objects.equals(it.getSubject(), subject);
                        } return true;
                    }
                ).filter( it -> {
                        if(type!= null && !type.isEmpty()){
                            return Objects.equals(it.getType(), type);
                        } return true;
                    }
                ).filter( it -> {
                            if(text!= null && !text.isEmpty()){
                                return it.getTitle().contains(text) || it.getDescription().contains(text);
                            } return true;
                        }
                ).filter( it -> {
                            if(epoch!= null && !epoch.isEmpty()){
                                return Objects.equals(it.getEpoch(), epoch);
                            } return true;
                        }
                ).filter( it -> {
                            if(mediaType!= null && !mediaType.isEmpty()){
                                return Objects.equals(it.getMediaType(), mediaType);
                            } return true;
                        }
                )
                .collect(Collectors.toList());
        if (publications.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(publications);
    }

}

package com.einoorish.exhibitionsbackend.service;

import com.einoorish.exhibitionsbackend.controller.exhibits.dto.ExhibitRequest;
import com.einoorish.exhibitionsbackend.model.category.Category;
import com.einoorish.exhibitionsbackend.model.exhibit.Exhibit;
import com.einoorish.exhibitionsbackend.model.user.User;
import com.einoorish.exhibitionsbackend.repository.exhibit.ExhibitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

@Service
public class ExhibitService {
    @Autowired
    private ExhibitRepository exhibitRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private OrganizationsService organizationsService;

    @Autowired
    private S3Service s3Service;

    public void saveData(ExhibitRequest data) throws IOException {
        if(data.getId() != null) {
            // updating existing exhibit
            Exhibit exhibit = exhibitRepository.getById(data.getId());
            s3Service.deleteFile(exhibit.getFile());
            s3Service.deleteFile(exhibit.getThumbnail());
        }

        String thumbnailUrl = s3Service.saveFile(data.getThumbnail());
        String fileUrl = s3Service.saveFile(data.getFile());

        Exhibit exhibit = new Exhibit(data, thumbnailUrl, fileUrl, userService.getCurrentUser().getId());

        exhibitRepository.save(exhibit);
    }

    public List<Exhibit> getAllForCurrentUser() {
        s3Service.createRulesForAccess();
        return getAllForUser(userService.getCurrentUser().getId());
    }

    public Exhibit getById(Long id) {
        Exhibit exhibit = exhibitRepository.findById(id).orElse(null);
        if (exhibit != null) {
            User user = userService.getById(exhibit.getUserId());
            exhibit.setOrganization(organizationsService.findById(exhibit.getUserId()).getOrganizationName());

            exhibit.setThumbnailUrl(s3Service.getUrlForFileName(exhibit.getThumbnail(), user.getUsername()));
            exhibit.setFileUrl(s3Service.getUrlForFileName(exhibit.getFile(), user.getUsername()));
        }
        return exhibit;
    }

    public List<Exhibit> getAll() {
        List<Exhibit> exhibits = exhibitRepository.findAll();
        exhibits.forEach(it -> {
                    it.setThumbnailUrl(s3Service.getUrlForFileName(it.getThumbnail()));
                    it.setFileUrl(s3Service.getUrlForFileName(it.getFile()));
                }
        );
        return exhibits;
    }

    public List<Exhibit> getAllForUser(Long authorId) {
        List<Exhibit> exhibits = exhibitRepository.findByUserId(authorId);
        User user = userService.getById(authorId);
        exhibits.forEach(it -> {
                    it.setOrganization(organizationsService.findById(it.getUserId()).getOrganizationName());
                    it.setThumbnailUrl(s3Service.getUrlForFileName(it.getThumbnail(), user.getUsername()));
                    it.setFileUrl(s3Service.getUrlForFileName(it.getFile(), user.getUsername()));
                }
        );
        return exhibits;
    }

    public void delete(Long id) {
        Exhibit exhibit = exhibitRepository.findById(id).orElse(null);
        if (exhibit != null) {
            s3Service.deleteFile(exhibit.getFile());
            s3Service.deleteFile(exhibit.getThumbnail());

            exhibitRepository.deleteById(id);
        }
    }
}
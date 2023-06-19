package com.einoorish.exhibitionsbackend.controller.exhibits.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.lang.reflect.Field;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ExhibitRequest {
    private Long id;
    private String title;
    private String description;
    private MultipartFile thumbnail;
    private String subject;
    private String type;
    private String epoch;
    private String mediaType;
    private MultipartFile file;
}

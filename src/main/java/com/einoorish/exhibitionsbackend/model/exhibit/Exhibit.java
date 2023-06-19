package com.einoorish.exhibitionsbackend.model.exhibit;

import com.einoorish.exhibitionsbackend.controller.exhibits.dto.ExhibitRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "exhibit")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Exhibit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id")
    private Long userId;


    private String title;
    
    private String description;

    private String thumbnail;

    @Transient
    private String thumbnailUrl;

    private String subject;

    private String type;

    private String epoch;

    @Column(name = "media_type")
    private String mediaType;

    private String file;

    @Transient
    private String fileUrl;

    @Transient
    private String organization;

    public Exhibit(ExhibitRequest data, String thumbnailUrl, String fileUrl, Long userId) {
        this.id = data.getId();
        this.title = data.getTitle();
        this.description = data.getDescription();
        this.userId = userId;
        this.thumbnail = thumbnailUrl;
        this.subject = data.getSubject();
        this.type = data.getType();
        this.epoch = data.getEpoch();
        this.file = fileUrl;
        this.mediaType = data.getMediaType();
    }
}

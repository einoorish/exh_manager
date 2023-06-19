package com.einoorish.exhibitionsbackend.controller.exhibits;

import com.einoorish.exhibitionsbackend.model.exhibit.*;
import com.einoorish.exhibitionsbackend.controller.exhibits.dto.ExhibitRequest;
import com.einoorish.exhibitionsbackend.service.ExhibitService;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.print.attribute.standard.Media;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Controller
public class ExhibitsController {

    @Autowired
    ExhibitService exhibitService;


    @GetMapping("/exhibits")
    @PreAuthorize("hasAuthority('PUBLISHER')")
    public String userPage(Model model, @RequestParam(required = false) String type, @RequestParam(required = false) String mediaType, @RequestParam(required = false) String subject, @RequestParam(required = false) String epoch) {
        String selectedType = type == null ? "" : type;
        String selectedMediaType = mediaType == null ? "" : mediaType;
        String selectedSubject = subject == null ? "" : subject;
        String selectedEpoch = epoch == null ? "" : epoch;
        model.addAttribute("selectedType", selectedType);
        model.addAttribute("selectedMediaType", selectedMediaType);
        model.addAttribute("selectedSubject", selectedSubject);
        model.addAttribute("selectedEpoch", selectedEpoch);

        ArrayList<ImmutablePair<String, String>> epochs = Arrays.stream(Epoch.values()).map(it -> new ImmutablePair<>(it.name(), it.getUkr())).collect(Collectors.toCollection(ArrayList::new));
        epochs.add(0, new ImmutablePair<>("", "УСІ"));
        epochs.remove(new ImmutablePair<>(selectedEpoch, selectedEpoch.equals("") ? "УСІ" : Epoch.valueOf(selectedEpoch).getUkr()));
        epochs.add(0,  new ImmutablePair<>(selectedEpoch, selectedEpoch.equals("") ? "УСІ" : Epoch.valueOf(selectedEpoch).getUkr()));
        model.addAttribute("epochs", epochs);

        ArrayList<ImmutablePair<String, String>> subjects = Arrays.stream(ExhibitSubjects.values()).map(it -> new ImmutablePair<>(it.name(), it.getUkr())).collect(Collectors.toCollection(ArrayList::new));
        subjects.add(0, new ImmutablePair<>("", "УСІ"));
        subjects.remove(new ImmutablePair<>(selectedSubject, selectedSubject.equals("") ? "УСІ" : ExhibitSubjects.valueOf(selectedSubject).getUkr()));
        subjects.add(0,  new ImmutablePair<>(selectedSubject, selectedSubject.equals("") ? "УСІ" : ExhibitSubjects.valueOf(selectedSubject).getUkr()));
        model.addAttribute("subjects", subjects);

        ArrayList<ImmutablePair<String, String>> types = Arrays.stream(ExhibitType.values()).map(it -> new ImmutablePair<>(it.name(), it.getUkr())).collect(Collectors.toCollection(ArrayList::new));
        types.add(0, new ImmutablePair<>("", "УСІ"));
        types.remove(new ImmutablePair<>(selectedType, selectedType.equals("") ? "УСІ" : ExhibitType.valueOf(selectedType).getUkr()));
        types.add(0,  new ImmutablePair<>(selectedType, selectedType.equals("") ? "УСІ" : ExhibitType.valueOf(selectedType).getUkr()));
        model.addAttribute("types", types);

        ArrayList<ImmutablePair<String, String>> mediaTypes = Arrays.stream(MediaType.values()).map(it -> new ImmutablePair<>(it.name(), it.getUkr())).collect(Collectors.toCollection(ArrayList::new));
        mediaTypes.add(0, new ImmutablePair<>("", "УСІ"));
        mediaTypes.remove(new ImmutablePair<>(selectedMediaType, selectedMediaType.equals("") ? "УСІ" : MediaType.valueOf(selectedMediaType).getUkr()));
        mediaTypes.add(0,  new ImmutablePair<>(selectedMediaType, selectedMediaType.equals("") ? "УСІ" : MediaType.valueOf(selectedMediaType).getUkr()));
        model.addAttribute("mediaTypes", mediaTypes);

        List<Exhibit> exhibits = exhibitService.getAllForCurrentUser().stream()
                .filter(exhibit -> {
                    if(mediaType != null && !mediaType.isEmpty()) {
                        return Objects.equals(exhibit.getMediaType(), mediaType);
                    } return true;
                }).filter(exhibit -> {
                    if(type != null && !type.isEmpty()) {
                        return Objects.equals(exhibit.getType(), type);
                    } return true;
                }).filter(exhibit -> {
                    if(subject != null && !subject.isEmpty()) {
                        return Objects.equals(exhibit.getSubject(), subject);
                    } return true;
                }).filter(exhibit -> {
                    if(epoch != null && !epoch.isEmpty()) {
                        return Objects.equals(exhibit.getEpoch(), epoch);
                    } return true;
                }).collect(Collectors.toList());

        model.addAttribute("exhibits", exhibits);

//
//        String selectedType = (String) model.getAttribute("selectedType");
//        String selectedMediaType = (String) model.getAttribute("selectedMediaType");
//        String selectedSubject = (String) model.getAttribute("selectedSubject");

        return "publisher/exhibits";
    }


    @GetMapping("/edit-exhibit/{id}")
    @PreAuthorize("hasAuthority('PUBLISHER')")
    public String addObjectPage(@PathVariable("id") Long id, Model model){
        Exhibit exhibit = exhibitService.getById(id);

        model.addAttribute("exhibit", exhibit);

        JSONObject fileJson = new JSONObject();
        fileJson.put("url", exhibit.getFileUrl());
        fileJson.put("type", exhibit.getMediaType());
        model.addAttribute("file_data", fileJson);

        model.addAttribute("exhibit_subject", ExhibitSubjects.valueOf(exhibit.getSubject()).makePair());
        model.addAttribute("exhibit_type", ExhibitType.valueOf(exhibit.getType()).makePair());
        model.addAttribute("exhibit_epoch", Epoch.valueOf(exhibit.getEpoch()).makePair());

        ArrayList<Epoch> epochs = Arrays.stream(Epoch.values()).collect(Collectors.toCollection(ArrayList::new));
        epochs.remove(Epoch.valueOf(exhibit.getEpoch()));
        epochs.add(0, Epoch.valueOf(exhibit.getEpoch()));
        model.addAttribute("epochs", epochs.stream().map(Epoch::makePair).collect(Collectors.toList()));

        ArrayList<ExhibitSubjects> subjects = new ArrayList<>(Arrays.asList(ExhibitSubjects.values()));
        subjects.remove(ExhibitSubjects.valueOf(exhibit.getSubject()));
        subjects.add(0, ExhibitSubjects.valueOf(exhibit.getSubject()));
        model.addAttribute("subjects", subjects.stream().map(ExhibitSubjects::makePair).collect(Collectors.toList()));

        ArrayList<ExhibitType> types = new ArrayList<>(Arrays.asList(ExhibitType.values()));
        types.remove(ExhibitType.valueOf(exhibit.getType()));
        types.add(0, ExhibitType.valueOf(exhibit.getType()));
        model.addAttribute("types", types.stream().map(ExhibitType::makePair).collect(Collectors.toList()));

        ArrayList<MediaType> mediaTypes = new ArrayList<>(Arrays.asList(MediaType.values()));
        mediaTypes.remove(MediaType.valueOf(exhibit.getMediaType()));
        mediaTypes.add(0, MediaType.valueOf(exhibit.getMediaType()));
        model.addAttribute("mediaTypes", mediaTypes.stream().map(MediaType::makePair).collect(Collectors.toList()));

        model.addAttribute("objectRequest", new ExhibitRequest());

        return "publisher/edit_exhibit";
    }

    @PostMapping("/edit-exhibit/{id}")
    @PreAuthorize("hasAuthority('PUBLISHER')")
    public String addObjectPage(ExhibitRequest objectRequest) throws IOException {
        exhibitService.saveData(objectRequest);
        return "redirect:/exhibits";
    }

    @GetMapping("/delete-exhibit/{id}")
    @PreAuthorize("hasAuthority('PUBLISHER')")
    public String deleteObject(@PathVariable Long id) throws IOException {
        exhibitService.delete(id);
        return "redirect:/exhibits";
    }

    @GetMapping("/add-exhibit")
    @PreAuthorize("hasAuthority('PUBLISHER')")
    public String addObjectPage(Model model){
        ArrayList<ImmutablePair<String, String>> subjects = Arrays.stream(ExhibitSubjects.values()).map(ExhibitSubjects::makePair).collect(Collectors.toCollection(ArrayList::new));
        model.addAttribute("subjects", subjects);
        ArrayList<ImmutablePair<String, String>> types = Arrays.stream(ExhibitType.values()).map(ExhibitType::makePair).collect(Collectors.toCollection(ArrayList::new));
        model.addAttribute("types", types);
        ArrayList<ImmutablePair<String, String>> epochs = Arrays.stream(Epoch.values()).map(Epoch::makePair).collect(Collectors.toCollection(ArrayList::new));
        model.addAttribute("epochs", epochs);
        ArrayList<ImmutablePair<String, String>> mediaTypes = Arrays.stream(MediaType.values()).map(MediaType::makePair).collect(Collectors.toCollection(ArrayList::new));
        model.addAttribute("mediaTypes", mediaTypes);

        model.addAttribute("objectRequest", new ExhibitRequest());

        return "publisher/add_object";
    }


    @PostMapping("/add-exhibit")
    @PreAuthorize("hasAuthority('PUBLISHER')")
    public String addObject(ExhibitRequest objectRequest) throws IOException {
        exhibitService.saveData(objectRequest);
        return "redirect:/exhibits";
    }

    @GetMapping("/exhibit/{id}")
    @PreAuthorize("hasAuthority('PUBLISHER')")
    public String viewObjectRequestDetails(@PathVariable("id") Long id, Model model){
        Exhibit exhibit = exhibitService.getById(id);

        model.addAttribute("exhibit", exhibit);

        JSONObject fileJson = new JSONObject();
        fileJson.put("url", exhibit.getFileUrl());
        fileJson.put("type", exhibit.getMediaType());
        model.addAttribute("file_data", fileJson);
        model.addAttribute("exhibit_subject", ExhibitSubjects.valueOf(exhibit.getSubject()).getUkr());
        model.addAttribute("exhibit_type", ExhibitType.valueOf(exhibit.getType()).getUkr());
        model.addAttribute("exhibit_epoch", Epoch.valueOf(exhibit.getEpoch()).getUkr());
        model.addAttribute("exhibit_media", MediaType.valueOf(exhibit.getMediaType()).getUkr());


        return "publisher/exhibit_details";
    }

    @GetMapping("/exhibit_media/{id}")
    public String getExhibitMedia(@PathVariable("id") Long id, Model model){
        Exhibit exhibit = exhibitService.getById(id);
        JSONObject fileJson = new JSONObject();
        fileJson.put("url", exhibit.getFileUrl());
        fileJson.put("type", exhibit.getMediaType());
        model.addAttribute("file_data", fileJson);

        return "public/media_view";
    }
}

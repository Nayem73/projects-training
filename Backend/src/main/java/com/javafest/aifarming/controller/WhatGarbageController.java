package com.javafest.aifarming.controller;

import com.javafest.aifarming.model.Garbage;
import com.javafest.aifarming.model.WhatGarbage;
import com.javafest.aifarming.repository.GarbageRepository;
import com.javafest.aifarming.repository.WhatGarbageRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@RestController
@RequestMapping("/api")
public class WhatGarbageController {
    private final WhatGarbageRepository whatGarbageRepository;
    private final GarbageRepository garbageRepository;

    public WhatGarbageController(WhatGarbageRepository whatGarbageRepository, GarbageRepository garbageRepository) {
        this.whatGarbageRepository = whatGarbageRepository;
        this.garbageRepository = garbageRepository;
    }

    @GetMapping("/whatgarbage/")
    public ResponseEntity<Page<Map<String, Object>>> getAllWhatGarbage(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<WhatGarbage> whatGarbagePage = whatGarbageRepository.findAll(pageable);

        List<Map<String, Object>> response = new ArrayList<>();

        for (WhatGarbage whatGarbage : whatGarbagePage.getContent()) {
            Map<String, Object> res = new LinkedHashMap<>();
            res.put("id", whatGarbage.getId());
            res.put("title", whatGarbage.getTitle());
            res.put("img", whatGarbage.getImg());
            res.put("garbage", whatGarbage.getGarbage());

            response.add(res);
        }

        return ResponseEntity.ok()
                .body(new PageImpl<>(response, pageable, whatGarbagePage.getTotalElements()));
    }

    @GetMapping("/whatgarbage/{garbageTitle}/{whatGarbageTitle}")
    public ResponseEntity<?> getGarbagesByCategoryTitleAndWhatGarbage(@PathVariable String garbageTitle, @PathVariable String whatGarbageTitle) {
        WhatGarbage whatGarbage = whatGarbageRepository.findByGarbageTitleAndWhatGarbageTitleExact(garbageTitle, whatGarbageTitle);
        if (whatGarbage == null || whatGarbage.getTitle().isEmpty()) {
            //return new ResponseEntity<>("WhatGarbage not found", HttpStatus.NOT_FOUND);
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("message", "WhatGarbage not found");
            return ResponseEntity.badRequest().body(errorResponse);
        }
        return new ResponseEntity<>(whatGarbage, HttpStatus.OK);
    }

    @GetMapping("/whatgarbage")
    public ResponseEntity<Page<WhatGarbage>> getGarbagesByWhatGarbage(
            @RequestParam(value = "garbage", required = false) String garbageTitle,
            @RequestParam(value = "whatGarbage", required = false) String whatGarbageTitle,
            @RequestParam(value = "search", required = false) String search,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<WhatGarbage> whatGarbages;
        if (garbageTitle != null) {
            // Case 1: garbage is provided, search is optional
            if (garbageTitle.isEmpty()) {
                whatGarbages = whatGarbageRepository.findBySearch(search, pageable);
            } else if ((search == null || search.isEmpty()) && (whatGarbageTitle == null || whatGarbageTitle.isEmpty())) {
                whatGarbages = whatGarbageRepository.findByTitle(garbageTitle, pageable);
            } else if (whatGarbageTitle != null) {
                WhatGarbage dis = whatGarbageRepository.findByGarbageTitleAndWhatGarbageTitleExact(garbageTitle, whatGarbageTitle);
                List<WhatGarbage> disList = Collections.singletonList(dis);
                whatGarbages = new PageImpl<>(disList, pageable, disList.size());
            }
            else {
                whatGarbages = whatGarbageRepository.findByCategoryTitleAndWhatGarbage(garbageTitle, search, pageable);
            }
        } else if (whatGarbageTitle != null) {
            whatGarbages = whatGarbageRepository.findByWhatGarbageTitle(whatGarbageTitle, pageable);
        } else if (search != null) {
            whatGarbages = whatGarbageRepository.findBySearch(search, pageable);
        } else {
            // Handle the case when both parameters are missing.
            // For example, return an error message or an empty list.
            whatGarbages = new PageImpl<>(Collections.emptyList());
        }
        return ResponseEntity.ok(whatGarbages);
    }

    @PostMapping("/whatgarbage/")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') OR hasAuthority('ROLE_SUPER_ADMIN')")
    public ResponseEntity<Map<String, Object>> addWhatGarbage(
            @RequestParam("title") String title,
            @RequestParam("img") MultipartFile file,
            @RequestParam("description") String description,
            @RequestParam("garbageId") Long garbageId
    ) throws IOException {
        Garbage garbage = garbageRepository.findById(garbageId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid garbage ID: " + garbageId));



        if (file.isEmpty()) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("message", "Please select an image");
            return ResponseEntity.badRequest().body(errorResponse);
        } else if (title.isEmpty()) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("message", "Please select a title for the WhatGarbage");
            return ResponseEntity.badRequest().body(errorResponse);
        }  else if (garbageId == null) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("message", "Please select a garbageId for the WhatGarbage");
            return ResponseEntity.badRequest().body(errorResponse);
        }

        // Check if the uploaded file is an image
        if (!isImageFile(file)) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("message", "Only image files are allowed.");
            return ResponseEntity.badRequest().body(errorResponse);
        }

        // Set the appropriate path to store the image (adjust this to your needs)
        String imagePath = "src/main/resources/images";

        // Create the directory if it doesn't exist
        Path imageDir = Paths.get(imagePath);
        if (!Files.exists(imageDir)) {
            Files.createDirectories(imageDir);
        }

        // Generate a unique file name for the image
        String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();

        // Save the image file using the provided path
        Path targetPath = imageDir.resolve(fileName);
        Files.copy(file.getInputStream(), targetPath);

        // Set the image path to the WhatGarbage object
//        WhatGarbage.setImg(targetPath.toString());
        WhatGarbage whatGarbage = new WhatGarbage(title, "/api/picture?link=images/" + fileName, description, garbage);

        // Save the WhatGarbage object to the database
        whatGarbageRepository.save(whatGarbage);
        //save notifications for all users
        //notificationService.saveWhatGarbageNotificationForAllUsers(title, WhatGarbage.getTitle());

        // Create a response with the link to the uploaded image
        Map<String, Object> response = new HashMap<>();
        response.put("successfully added!", whatGarbage);

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    private boolean isImageFile(MultipartFile file) {
        String fileName = file.getOriginalFilename();
        return fileName != null && (fileName.endsWith(".jpg") || fileName.endsWith(".jpeg") || fileName.endsWith(".png"));
    }

}

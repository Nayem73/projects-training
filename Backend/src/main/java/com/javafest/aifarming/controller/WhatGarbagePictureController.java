package com.javafest.aifarming.controller;

import com.javafest.aifarming.model.WhatGarbage;
import com.javafest.aifarming.model.WhatGarbagePicture;
import com.javafest.aifarming.repository.WhatGarbagePictureRepository;
import com.javafest.aifarming.repository.WhatGarbageRepository;
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
public class WhatGarbagePictureController {
    private final WhatGarbagePictureRepository whatGarbagePictureRepository;
    private final WhatGarbageRepository whatGarbageRepository;

    public WhatGarbagePictureController(WhatGarbagePictureRepository whatGarbagePictureRepository, WhatGarbageRepository whatGarbageRepository) {
        this.whatGarbagePictureRepository = whatGarbagePictureRepository;
        this.whatGarbageRepository = whatGarbageRepository;
    }

    @GetMapping("/whatgarbage/{whatGarbageId}/picture/")
    public ResponseEntity<?> getAllWhatGarbagePicturesByWhatGarbageId(@PathVariable Long whatGarbageId) {
        // Fetch the corresponding whatGarbage object from the database using the whatGarbageId
        List<WhatGarbagePicture> existingWhatGarbagePictures = whatGarbagePictureRepository.findByAllwhatGarbagePictureById(whatGarbageId);
        if (existingWhatGarbagePictures.isEmpty()) {
            // If the whatGarbageId does not match any existing whatGarbage, return a response with the "picture not found" message
            Map<String, String> responseMessage = new HashMap<>();
            responseMessage.put("message", "Picture not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseMessage);
        }

        // Create a list to store the simplified representations of whatGarbagePicture objects
        List<Map<String, Object>> simplifiedWhatGarbagePictures = new ArrayList<>();

        // Iterate through the whatGarbagePicture entities and extract the specific fields to include in the response
        for (WhatGarbagePicture whatGarbagePicture : existingWhatGarbagePictures) {
            Map<String, Object> simplifiedPicture = new LinkedHashMap<>();
            simplifiedPicture.put("id", whatGarbagePicture.getId());
            simplifiedPicture.put("img", whatGarbagePicture.getImg());
            simplifiedWhatGarbagePictures.add(simplifiedPicture);
        }

        return ResponseEntity.ok().body(simplifiedWhatGarbagePictures);
    }

    @PostMapping("/whatgarbage/picture/")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') OR hasAuthority('ROLE_SUPER_ADMIN')")
    public ResponseEntity<?> addwhatGarbagePicture(
            @RequestParam("img") MultipartFile file,
            @RequestParam("whatGarbageId") Long whatGarbageId) throws IOException {

        if (file.isEmpty()) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("message", "Please select an image file.");
            return ResponseEntity.badRequest().body(errorResponse);
        } else if (whatGarbageId == null) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("message", "Please select a whatGarbage");
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

        // Fetch the corresponding whatGarbage object from the database using the whatGarbageId
        Optional<WhatGarbage> optionalwhatGarbage = whatGarbageRepository.findById(whatGarbageId);
        if (!optionalwhatGarbage.isPresent()) {
            // If the whatGarbageId does not match any existing whatGarbage, return an error response
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("message", "Invalid whatGarbageId. No matching whatGarbage found.");
            return ResponseEntity.badRequest().body(errorResponse);
        }

        WhatGarbage whatGarbage = optionalwhatGarbage.get();

        // Create a new whatGarbagePicture object with the image path and the fetched whatGarbage object
        WhatGarbagePicture whatGarbagePicture = new WhatGarbagePicture("/api/picture?link=images/" + fileName, whatGarbage);
        whatGarbagePictureRepository.save(whatGarbagePicture);

        // Create a response with the picture ID and image URL
        Map<String, Object> response = new HashMap<>();
        response.put("link", "/api/picture?link=images/" + fileName);

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    private boolean isImageFile(MultipartFile file) {
        String fileName = file.getOriginalFilename();
        return fileName != null && (fileName.endsWith(".jpg") || fileName.endsWith(".jpeg") || fileName.endsWith(".png"));
    }
}

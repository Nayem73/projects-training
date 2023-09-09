package com.javafest.aifarming.controller;

import com.javafest.aifarming.model.Garbage;
import com.javafest.aifarming.repository.GarbageRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/garbage")
public class GarbageController {
    private final GarbageRepository garbageRepository;

    public GarbageController(GarbageRepository garbageRepository) {
        this.garbageRepository = garbageRepository;
    }


    @GetMapping("/")
    public List<Garbage> getAllGarbage() {
        return garbageRepository.findAll();
    }

    @PostMapping("/")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') OR hasAuthority('ROLE_SUPER_ADMIN')")
    public ResponseEntity<?> addGarbage(@RequestParam String title) {
        Garbage existingGarbage = garbageRepository.findByTitle(title);
        Map<String, Object> response = new HashMap<>();

        if (existingGarbage == null) {
            Garbage newGarbage = new Garbage();
            newGarbage.setTitle(title);
//            Garbage savedGarbage = garbageRepository.save(newGarbage);
            garbageRepository.save(newGarbage);

            response.put("message", "Garbage added successfully");
            response.put("Garbage", newGarbage);
            return ResponseEntity.ok(response);
        } else {
            response.put("message", "Garbage with title '" + title + "' already exists.");
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') OR hasAuthority('ROLE_SUPER_ADMIN')")
    public ResponseEntity<Map<String, Object>> updateGarbage(@PathVariable Long id, @RequestParam String title) {
        Garbage garbage = garbageRepository.findGarbageById(id);
        Garbage existingGarbage = garbageRepository.findByTitle(title);
        Map<String, Object> response = new HashMap<>();
        if (garbage == null) {
            response.put("message", "Invalid Garbage Id");
            return ResponseEntity.badRequest().body(response);
        } else if (existingGarbage != null) {
            response.put("message", "Garbage with title '" + title + "' already exists.");
            return ResponseEntity.badRequest().body(response);
        }

        garbage.setTitle(title);
        garbageRepository.save(garbage);

        response.put("message", "Garbage updated successfully");
        response.put("Garbage", garbage);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') OR hasAuthority('ROLE_SUPER_ADMIN')")
    public ResponseEntity<Map<String, Object>> deleteGarbage(@PathVariable Long id) {
        Garbage garbage = garbageRepository.findGarbageById(id);
        Map<String, Object> response = new HashMap<>();

        if (garbage == null) {
            response.put("message", "Invalid Garbage Id");
            return ResponseEntity.badRequest().body(response);
        } else {
            garbageRepository.delete(garbage);
            response.put("message", "Garbage deleted successfully");
            return ResponseEntity.ok(response);
        }
    }
}

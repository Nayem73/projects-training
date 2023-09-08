package com.javafest.aifarming.repository;

import com.javafest.aifarming.model.Garbage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GarbageRepository extends JpaRepository<Garbage, Long> {
    Garbage findGarbageById(Long id);

    Garbage findByTitle(String title);
}

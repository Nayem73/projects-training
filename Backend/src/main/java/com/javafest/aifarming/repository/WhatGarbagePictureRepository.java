package com.javafest.aifarming.repository;

import com.javafest.aifarming.model.WhatGarbagePicture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WhatGarbagePictureRepository extends JpaRepository<WhatGarbagePicture, Long> {
    @Query("SELECT c FROM WhatGarbagePicture c JOIN FETCH c.whatGarbage cc WHERE cc.id = ?1")
    List<WhatGarbagePicture> findByAllwhatGarbagePictureById(Long whatGarbageId);

}

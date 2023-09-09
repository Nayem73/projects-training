package com.javafest.aifarming.repository;

import com.javafest.aifarming.model.WhatGarbage;
import com.javafest.aifarming.model.WhatGarbagePicture;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WhatGarbageRepository extends JpaRepository<WhatGarbage, Long> {
    @Query("SELECT c FROM WhatGarbage c WHERE c.title = ?1")
    Page<WhatGarbage> findByWhatGarbageTitle(String whatGarbage, Pageable pageable);

    @Query("SELECT c FROM WhatGarbage c JOIN FETCH c.garbage cc WHERE cc.title = ?1 AND c.title LIKE %?2%")
    Page<WhatGarbage> findByCategoryTitleAndWhatGarbage(String categoryTitle, String search, Pageable pageable);

    @Query("SELECT c FROM WhatGarbage c JOIN FETCH c.garbage cc WHERE cc.id = ?1 AND c.title = ?2")
    List<WhatGarbage> findByGarbageIdAndWhatGarbageExact(Long garbageId, String whatGarbageTitle);

    @Query("SELECT c FROM WhatGarbage c JOIN FETCH c.garbage cc WHERE cc.title = ?1 AND c.title = ?2")
    WhatGarbagePicture findByGarbageTitleAndWhatGarbageTitleExact(String garbageTitle, String whatGarbageTitle);

    @Query("SELECT c FROM WhatGarbage c JOIN FETCH c.garbage cc WHERE cc.title = ?1")
    Page<WhatGarbage> findByTitle(String categoryTitle, Pageable pageable);

    @Query("SELECT c FROM WhatGarbage c JOIN FETCH c.garbage cc WHERE c.title LIKE %?1% OR cc.title LIKE %?1%")
    Page<WhatGarbage> findBySearch(String keyword, Pageable pageable);

}

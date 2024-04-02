package com.pfm.category.repository;

import com.pfm.category.repository.model.KeywordEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface KeywordRepository extends JpaRepository<KeywordEntity, Long> {
    List<KeywordEntity> findByValueContaining(String keyword);

    // Full-text search
    @Query(value = "SELECT * FROM pfm_keyword WHERE MATCH(value) AGAINST (:description IN NATURAL LANGUAGE MODE)", nativeQuery = true)
    List<KeywordEntity> searchWithNaturalLanguageMode(@Param("description") String searchTerm);
}

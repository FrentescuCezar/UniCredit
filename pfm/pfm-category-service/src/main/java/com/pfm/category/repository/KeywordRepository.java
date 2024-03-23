package com.pfm.category.repository;

import com.pfm.category.repository.model.KeywordEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface KeywordRepository extends JpaRepository<KeywordEntity, Long> {
    List<KeywordEntity> findByValueContaining(String keyword);
}

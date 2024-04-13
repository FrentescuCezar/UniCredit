package com.pfm.category.repository;

import com.pfm.category.repository.model.CategoryEntity;
import com.pfm.category.service.dto.CategoryDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<CategoryEntity, Long> {
    @Query("SELECT new com.pfm.category.service.dto.CategoryDTO(c.id, c.parent.id, c.value) FROM CategoryEntity c WHERE c.parent IS NULL")
    List<CategoryDTO> findAllTopLevelCategories();

    @Query("SELECT new com.pfm.category.service.dto.CategoryDTO(c.id, c.parent.id, c.value) FROM CategoryEntity c WHERE c.parent.id = ?1")
    List<CategoryDTO> findAllByParentId(Long parentId);
}

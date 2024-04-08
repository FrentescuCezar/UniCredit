package com.pfm.category.service;

import com.pfm.category.service.dto.TransactionDTO;
import com.pfm.category.repository.KeywordRepository;
import com.pfm.category.repository.model.KeywordEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final KeywordRepository keywordRepository;
    private final KeywordService keywordService;

    public Optional<Long> findCategoryForTransaction(TransactionDTO transaction) {
        List<KeywordEntity> keywordList = findCategoryForTransactionWithoutPattern(transaction.getDescription());
        return determineCategory(keywordList);
    }

    public List<KeywordEntity> findCategoryForTransactionWithoutPattern(String description) {
        return keywordRepository.searchWithNaturalLanguageMode(description);
    }
    private Optional<Long> determineCategory(List<KeywordEntity> keywords) {
        if (keywords.isEmpty()) {
            return Optional.empty();
        }

        Map<Long, Long> categoryCounts = keywords.stream()
                .collect(Collectors.groupingBy(keyword -> keyword.getCategory().getId(), Collectors.counting()));

        Long mostCommonCategoryId = categoryCounts.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(null);

        if (mostCommonCategoryId != null) {
            return Optional.of(mostCommonCategoryId);
        }

        // If no common category ID, find most common parent category ID
        Map<Long, Long> parentCategoryCounts = keywords.stream()
                .map(keyword -> keyword.getCategory().getParent())
                .filter(Objects::nonNull)
                .collect(Collectors.groupingBy(parentCategory -> parentCategory.getId(), Collectors.counting()));

        Optional<Long> mostCommonParentCategoryId = parentCategoryCounts.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey);


        return mostCommonParentCategoryId;
    }



}
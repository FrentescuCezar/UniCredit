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
        Optional<Long> categoryId = findCategoryForTransactionWithPattern(transaction.getDescription());
        if (categoryId.isPresent()) {
            return categoryId;
        }

        List<KeywordEntity> keywordList = findCategoryForTransactionWithoutPattern(transaction.getDescription());
        return determineCategory(keywordList);
    }
    private Optional<Long> findCategoryForTransactionWithPattern(String description) {
        List<String> keywords = tokenizeAndCleanDescription(description);

        // Extract keywords from the transaction description,
        // search for related categories, and return the category
        // ID with the highest occurrence count

        return keywords.stream()
                .flatMap(keyword -> keywordRepository.findByValueContaining(keyword).stream())
                .collect(Collectors.groupingBy(keyword -> keyword.getCategory().getId(), Collectors.counting()))
                .entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey);
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

    private List<String> tokenizeAndCleanDescription(String description) {
        Optional<String> optionalKeywords = keywordService.parseTransactionDescription(description);
        if (optionalKeywords.isEmpty()) {
            return Collections.emptyList(); // or however you want to handle missing keywords
        }
        String keywords = optionalKeywords.get();
        String cleanedDescription = keywords.replaceAll("[^a-zA-Z ]", "").toLowerCase();
        List<String> initialKeywords = Arrays.asList(cleanedDescription.split("\\s+"));
        return keywordService.filterKeywords(initialKeywords);
    }


}
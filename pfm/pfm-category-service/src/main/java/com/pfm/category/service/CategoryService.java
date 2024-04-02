package com.pfm.category.service;

import com.pfm.category.service.dto.TransactionDTO;
import com.pfm.category.repository.KeywordRepository;
import com.pfm.category.repository.model.KeywordEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final KeywordRepository keywordRepository;
    private final KeywordService keywordService;

    public Optional<Long> findCategoryForTransaction(TransactionDTO transaction) {
        //Optional<Long> categoryId = findCategoryForTransactionWithPattern(transaction.getDescription());
        //if(categoryId.isPresent()){
        //    return categoryId;
        //}

        List<KeywordEntity> keywordList = findCategoryForTransactionWithoutPattern(transaction.getDescription());
        return determineCategory(keywordList);
    }




    private Optional<Long> findCategoryForTransactionWithPattern (String description){

        List<String> transactionKeywords = tokenizeAndCleanDescription(description);

        Map<Long, Integer> categoryScores = new HashMap<>(); // Category ID to score

        for (String trKeyword : transactionKeywords) {
            List<KeywordEntity> matchingKeywords = keywordRepository.findByValueContaining(trKeyword);
            for (KeywordEntity matchingKeyword : matchingKeywords) {
                Long categoryId = matchingKeyword.getCategory().getId();
                categoryScores.put(categoryId, categoryScores.getOrDefault(categoryId, 0) + 1);
            }
        }

        return categoryScores.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey);
    }
    public List<KeywordEntity> findCategoryForTransactionWithoutPattern (String description){
        return keywordRepository.searchWithNaturalLanguageMode(description);
    }



    public Optional<Long> determineCategory(List<KeywordEntity> keywordEntities) {
        if (keywordEntities.isEmpty()) {
            return Optional.empty();
        }

        if (keywordEntities.size() == 1) {
            return Optional.of(keywordEntities.get(0).getCategory().getId());
        }

        // Map to count occurrences of each category ID
        Map<Long, Long> categoryIdCounts = new HashMap<>();
        for (KeywordEntity keyword : keywordEntities) {
            Long categoryId = keyword.getCategory().getId();
            categoryIdCounts.put(categoryId, categoryIdCounts.getOrDefault(categoryId, 0L) + 1);
        }

        // Find the most common category ID
        Long mostCommonCategoryId = null;
        long maxCount = 1;
        for (Map.Entry<Long, Long> entry : categoryIdCounts.entrySet()) {
            if (entry.getValue() > maxCount) {
                mostCommonCategoryId = entry.getKey();
                maxCount = entry.getValue();
            }
        }

        if (mostCommonCategoryId != null) {
            return Optional.of(mostCommonCategoryId);
        }

        // If no common category ID, find the most common parent category ID
        Map<Long, Long> parentCategoryIdCounts = new HashMap<>();
        for (KeywordEntity keyword : keywordEntities) {
            Long parentCategoryId = keyword.getCategory().getParent().getId();
            if (parentCategoryId != null) {
                parentCategoryIdCounts.put(parentCategoryId, parentCategoryIdCounts.getOrDefault(parentCategoryId, 0L) + 1);
            }
        }

        // Find the most common parent category ID
        Long mostCommonParentCategoryId = null;
        maxCount = 0;
        for (Map.Entry<Long, Long> entry : parentCategoryIdCounts.entrySet()) {
            if (entry.getValue() > maxCount) {
                mostCommonParentCategoryId = entry.getKey();
                maxCount = entry.getValue();
            }
        }

        // Return the most common parent category ID wrapped in an Optional
        return mostCommonParentCategoryId != null ? Optional.of(mostCommonParentCategoryId) : Optional.empty();
    }

    private List<String> tokenizeAndCleanDescription(String description) {
        String keywords = keywordService.parseTransactionDescription(description).getKeywords();
        String cleanedDescription = keywords.replaceAll("[^a-zA-Z ]", "").toLowerCase();
        List<String> initialKeywords = Arrays.asList(cleanedDescription.split("\\s+"));
        return keywordService.filterKeywords(initialKeywords);
    }



}
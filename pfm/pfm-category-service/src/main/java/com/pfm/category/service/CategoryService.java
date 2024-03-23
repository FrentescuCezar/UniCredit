package com.pfm.category.service;

import com.pfm.category.service.dto.TransactionDTO;
import com.pfm.category.repository.CategoryRepository;
import com.pfm.category.repository.KeywordRepository;
import com.pfm.category.repository.model.KeywordEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final KeywordRepository keywordRepository;
    private final KeywordService keywordService;

    public Optional<Long> findCategoryForTransaction(TransactionDTO transaction) {
        List<String> transactionKeywords = tokenizeAndCleanDescription(transaction.getDescription());

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

    private List<String> tokenizeAndCleanDescription(String description) {
        String keywords = keywordService.parseTransactionDescription(description).getKeywords();
        String cleanedDescription = keywords.replaceAll("[^a-zA-Z ]", "").toLowerCase();
        List<String> initialKeywords = Arrays.asList(cleanedDescription.split("\\s+"));
        return keywordService.filterKeywords(initialKeywords);
    }



}
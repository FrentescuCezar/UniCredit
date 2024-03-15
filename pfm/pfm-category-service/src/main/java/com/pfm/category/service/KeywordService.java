package com.pfm.category.service;

import com.pfm.category.repository.KeywordRepository;
import com.pfm.category.utilty.TransactionDescriptionParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class KeywordService {
    private final KeywordRepository keywordRepository;
    private final TransactionDescriptionParser parser;

    @Autowired
    public KeywordService(KeywordRepository keywordRepository, TransactionDescriptionParser parser) {
        this.keywordRepository = keywordRepository;
        this.parser = parser;
    }

    public TransactionDescriptionParser.TransactionParts parseTransactionDescription(String description) {
        return parser.parse(description);
    }

    public List<String> filterKeywords(List<String> keywords) {
        Set<String> filterWords = new HashSet<>(Arrays.asList("srl", "sa", "romania", "sc", "impex"));
        return keywords.stream()
                .filter(keyword -> !filterWords.contains(keyword.toLowerCase()))
                .collect(Collectors.toList());
    }

}

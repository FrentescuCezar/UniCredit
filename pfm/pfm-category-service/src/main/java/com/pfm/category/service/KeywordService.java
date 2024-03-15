package com.pfm.category.service;

import com.pfm.category.repository.KeywordRepository;
import com.pfm.category.utilty.TransactionDescriptionParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

}

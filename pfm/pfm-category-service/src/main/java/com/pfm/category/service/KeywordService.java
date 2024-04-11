package com.pfm.category.service;


import com.pfm.category.repository.KeywordRepository;
import com.pfm.category.repository.model.KeywordEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class KeywordService {
    @Autowired
    KeywordRepository keywordRepository;

    public ArrayList<String> filterKeywords(String keyword) {
        Set<String> filterWords = new HashSet<>(Arrays.asList("srl", "sa", "romania", "sc", "impex"));
        if (!filterWords.contains(keyword)) {
            return new ArrayList<>();
        }
        ArrayList<KeywordEntity> elementsFound = new ArrayList<>(keywordRepository.findByValueContaining(keyword));
        ArrayList<String> elementsFiltered = new ArrayList<>();
        for (KeywordEntity element : elementsFound) {
            String[] splitList = element.getValue().split(" ");
            StringJoiner joiner = new StringJoiner(" ");
            for (int index = 0; index < splitList.length - 1; index++) {
                joiner.add(splitList[index]);
            }
            elementsFiltered.add(joiner.toString());
        }
        return elementsFiltered;
    }
}

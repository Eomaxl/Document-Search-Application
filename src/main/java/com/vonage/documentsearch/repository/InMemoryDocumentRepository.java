package com.vonage.documentsearch.repository;

import com.vonage.documentsearch.model.Document;
import org.springframework.stereotype.Repository;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

@Repository
public class InMemoryDocumentRepository implements DocumentRepository {
    private final List<Document> documents = new ArrayList<>();

    @Override
    public void indexDocuments(List<Document> documents) {
        this.documents.addAll(documents);
    }

    @Override
    public Map<String, BigDecimal> searchDocuments(String query) {
        Map<String, Double> results = new HashMap<>();
        List<String> queryWords = Arrays.asList(query.toLowerCase().split("\\s+"));

        for (Document document : documents) {
            List<String> contentWords = Arrays.asList(document.getContent().toLowerCase().split("\\s+"));
            long matchCount = queryWords.stream()
                    .filter(contentWords::contains)
                    .count();

            BigDecimal matchScore = calculateMatchScore(queryWords.size(), matchCount);
            if (matchScore.compareTo(BigDecimal.ZERO) > 0) {
                results.put(document.getFilename(), matchScore);
            }
        }

        return results.entrySet().stream()
                .sorted(Map.Entry.<String, BigDecimal>comparingByValue().reversed())
                .limit(10)
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1,
                        LinkedHashMap::new
                ));
    }

    private BigDecimal calculateMatchScore(int queryWordsSize, long matchCount) {
        if (matchCount == queryWordsSize) {
            return BigDecimal.valueOf(100).setScale(2, RoundingMode.HALF_UP);
        } else if (matchCount == 0) {
            return BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
        } else {
            return BigDecimal.valueOf((double) matchCount / queryWordsSize * 100)
                    .setScale(2, RoundingMode.HALF_UP);
        }
    }
}

package com.vonage.documentsearch.repository;

import com.vonage.documentsearch.model.Document;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface DocumentRepository {
    void indexDocuments(List<Document> documents);
    Map<String, BigDecimal> searchDocuments(String query);
}

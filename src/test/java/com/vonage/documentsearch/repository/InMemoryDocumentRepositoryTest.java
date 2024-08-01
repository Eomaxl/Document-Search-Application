package com.vonage.documentsearch.repository;

import com.vonage.documentsearch.model.Document;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class InMemoryDocumentRepositoryTest {

    private InMemoryDocumentRepository repository;

    @BeforeEach
    public void setUp() {
        repository = new InMemoryDocumentRepository();
    }

    @Test
    public void testIndexDocuments() {
        List<Document> documents = Arrays.asList(
                new Document("doc1.txt", "This is a test document"),
                new Document("doc2.txt", "Another test document")
        );

        repository.indexDocuments(documents);

        Map<String, BigDecimal> results = repository.searchDocuments("test");
        assertEquals(2, results.size());
    }

    @Test
    public void testSearchDocuments() {
        List<Document> documents = Arrays.asList(
                new Document("doc1.txt", "This is a test document"),
                new Document("doc2.txt", "Another test document"),
                new Document("doc3.txt", "This document does not contain the search term")
        );

        repository.indexDocuments(documents);

        Map<String, BigDecimal> results = repository.searchDocuments("test");
        assertEquals(2, results.size());
        assertEquals(100.0, results.get("doc1.txt"));
        assertEquals(100.0, results.get("doc2.txt"));
    }

    @Test
    public void testPartialMatchSearchDocuments() {
        List<Document> documents = Arrays.asList(
                new Document("doc1.txt", "This is a test document"),
                new Document("doc2.txt", "Another document for testing")
        );

        repository.indexDocuments(documents);

        Map<String, BigDecimal> results = repository.searchDocuments("test document");
        assertEquals(2, results.size());
        assertEquals(100.0, results.get("doc1.txt"));
        assertEquals(50.0, results.get("doc2.txt"));
    }
}

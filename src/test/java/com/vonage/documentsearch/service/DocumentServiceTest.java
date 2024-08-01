package com.vonage.documentsearch.service;

import com.vonage.documentsearch.repository.DocumentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

public class DocumentServiceTest {

    @Mock
    private DocumentRepository documentRepository;

    @InjectMocks
    private DocumentService documentService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testIndexDocumentsFromDirectory() throws IOException {
        Path tempDir = Files.createTempDirectory("testDocs");
        Path tempFile = Files.createTempFile(tempDir, "testFile", ".txt");
        Files.write(tempFile, Collections.singletonList("This is a test document"));

        documentService.indexDocumentsFromDirectory(tempDir.toString());

        verify(documentRepository, times(1)).indexDocuments(anyList());
    }

    @Test
    public void testSearchDocuments() {
        String query = "test";
        Map<String, BigDecimal> mockResults = new HashMap<>();
        mockResults.put("testFile.txt", BigDecimal.valueOf(100.0));

        when(documentRepository.searchDocuments(query)).thenReturn(mockResults);

        Map<String, BigDecimal> results = documentService.searchDocuments(query);

        assertEquals(1, results.size());
        assertEquals(100.0, results.get("testFile.txt"));
        verify(documentRepository, times(1)).searchDocuments(query);
    }
}

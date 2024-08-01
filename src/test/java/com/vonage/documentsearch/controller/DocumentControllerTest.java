package com.vonage.documentsearch.controller;

import com.vonage.documentsearch.service.DocumentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class DocumentControllerTest {

    @Mock
    private DocumentService documentService;

    @InjectMocks
    private DocumentController documentController;

    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(documentController).build();
    }

    @Test
    public void testIndexDocumentsFromDirectory() throws Exception {
        String directoryPath = "testDir";

        mockMvc.perform(post("/api/documents/index")
                        .param("directoryPath", directoryPath))
                .andExpect(status().isOk());

        verify(documentService, times(1)).indexDocumentsFromDirectory(directoryPath);
    }

    @Test
    public void testSearchDocuments() throws Exception {
        String query = "test";
        Map<String, BigDecimal> mockResults = new HashMap<>();
        mockResults.put("testFile.txt", BigDecimal.valueOf(100.0));

        when(documentService.searchDocuments(query)).thenReturn(mockResults);

        mockMvc.perform(get("/api/documents/search")
                        .param("query", query))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("{\"testFile.txt\":100.0}"));

        verify(documentService, times(1)).searchDocuments(query);
    }

    @Test
    public void testHealthCheck() throws Exception {
        mockMvc.perform(get("/api/documents/health"))
                .andExpect(status().isOk())
                .andExpect(content().string("Service is up and running!"));
    }
}

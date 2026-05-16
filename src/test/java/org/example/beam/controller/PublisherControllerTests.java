package org.example.beam.controller;

import org.example.beam.dto.CreatePublisherDto;
import org.example.beam.dto.ShowPublisherDto;
import org.example.beam.dto.UpdatePublisherDto;
import org.example.beam.repository.PublisherRepository;
import org.example.beam.service.PublisherService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PublisherControllerTests {

    @InjectMocks
    private PublisherController publisherController;


    @Mock
    private PublisherService publisherService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void addPublisher_success() {
        CreatePublisherDto dto = new CreatePublisherDto(
                "TestPub",
                "CountryX",
                "2000-01-01",
                "https://pub.com",
                "20"
        );
        String result = publisherController.addPublisher(dto);
        assertEquals("Publisher added successfully", result);
        verify(publisherService, times(1)).createPublisher(any(CreatePublisherDto.class));
    }



    @Test
    void deletePublisher_callsService() {
        Long id = 1L;
        publisherController.deletePublisher(id);
        verify(publisherService).deletePublisher(id);
    }

    @Test
    void updatePublisher_success() {
        Long id = 1L;
        UpdatePublisherDto dto = new UpdatePublisherDto("New Name", null, null, null, null);

        String result = publisherController.updatePublisher(id, dto);

        assertEquals("Publisher updated successfully", result);
        verify(publisherService).updatePublisher(id, dto);
    }

    @Test
    void getPublisher_success() {
        ShowPublisherDto dto = new ShowPublisherDto(3L,"EA", "USA", "40", "ea.com", "1982");

        when(publisherService.getPublisher(1L)).thenReturn(dto);

        ShowPublisherDto result = publisherController.getPublisher(1L);

        assertNotNull(result);
        assertEquals("EA", result.name());
        assertEquals("USA", result.country());
    }

    @Test
    void getPublisher_notFound() {
        when(publisherService.getPublisher(11L)).thenThrow(new RuntimeException("Publisher not found"));

        assertThrows(RuntimeException.class, () -> publisherController.getPublisher(11L));
    }
}
package org.example.beam.controller;

import org.example.beam.dto.CreatePublisherDto;
import org.example.beam.dto.ShowPublisherDto;
import org.example.beam.dto.UpdatePublisherDto;
import org.example.beam.model.Publisher;
import org.example.beam.repository.PublisherRepository;
import org.example.beam.service.PublisherService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PublisherControllerTests {

    @InjectMocks
    private PublisherController publisherController;

    @Mock
    private PublisherRepository publisherRepository;

    @Mock
    private PublisherService publisherService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void addPublisher_success() {
        CreatePublisherDto dto = new CreatePublisherDto();
        dto.setName("TestPub");
        dto.setCountry("CountryX");
        dto.setFounded("2000-01-01");
        dto.setWebsite("https://pub.com");
        dto.setYearsOfEstablishment("20");
        String result = publisherController.addPublisher(dto);
        assertEquals("Publisher added successfully", result);
        verify(publisherService, times(1)).createPublisher(any(CreatePublisherDto.class));
    }

    @Test
    void addPublisher_missingField() {
        CreatePublisherDto dto = new CreatePublisherDto();
        dto.setName("TestPub");
        dto.setCountry(null);
        dto.setFounded("2000-01-01");
        dto.setWebsite("https://pub.com");
        dto.setYearsOfEstablishment("20");
        String result = publisherController.addPublisher(dto);
        assertEquals("Please provide all the required fields", result);
        verify(publisherService, never()).createPublisher(any());
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
        UpdatePublisherDto dto = new UpdatePublisherDto();
        dto.setName("UpdatedPub");
        dto.setCountry("CountryY");
        dto.setFounded("1999-12-31");
        dto.setWebsite("https://upd.com");
        dto.setYearsOfEstablishment("25");
        String result = publisherController.updatePublisher(id, dto);
        assertEquals("Publisher updated successfully", result);
        verify(publisherService, times(1)).updatePublisher(eq(id), any(UpdatePublisherDto.class));
    }

    @Test
    void updatePublisher_missingField() {
        Long id = 2L;
        UpdatePublisherDto dto = new UpdatePublisherDto();
        dto.setName(null);
        dto.setCountry("CountryY");
        dto.setFounded("2001-01-01");
        dto.setWebsite("https://upd.com");
        dto.setYearsOfEstablishment("21");
        String result = publisherController.updatePublisher(id, dto);
        assertEquals("Please provide all the required fields", result);
        verify(publisherService, never()).updatePublisher(anyLong(), any());
    }

    @Test
    void getPublisher_success() {
        Long id = 10L;
        Publisher publisher = new Publisher();
        publisher.setName("TestPub");
        publisher.setCountry("ZZ");
        publisher.setFounded("1991");
        publisher.setWebsite("www.tst.com");
        publisher.setYearsOfEstablishment("33");

        when(publisherRepository.findById(id)).thenReturn(Optional.of(publisher));
        ShowPublisherDto dto = publisherController.getPublisher(id);

        assertEquals("TestPub", dto.getName());
        assertEquals("ZZ", dto.getCountry());
        assertEquals("1991", dto.getFounded());
        assertEquals("www.tst.com", dto.getWebsite());
        assertEquals("33", dto.getYearsOfEstablishment());
    }

    @Test
    void getPublisher_notFound() {
        Long id = 11L;
        when(publisherRepository.findById(id)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> publisherController.getPublisher(id));
    }
}
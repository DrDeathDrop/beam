package org.example.beam.service;

import org.example.beam.dto.*;
import org.example.beam.model.Publisher;
import org.example.beam.repository.PublisherRepository;
import org.junit.jupiter.api.*;
import org.mockito.*;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PublisherServiceTests {

    @InjectMocks
    private PublisherService publisherService;

    @Mock
    private PublisherRepository publisherRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createPublisher_success() {
        CreatePublisherDto dto = new CreatePublisherDto();
        dto.setName("Electronic Arts");
        dto.setCountry("USA");
        dto.setYearsOfEstablishment("40");
        dto.setWebsite("ea.com");
        dto.setFounded("1982");

        when(publisherRepository.save(any(Publisher.class))).thenAnswer(i -> i.getArguments()[0]);

        Publisher savedPublisher = publisherService.createPublisher(dto);

        assertNotNull(savedPublisher);
        assertEquals("Electronic Arts", savedPublisher.getName());
        assertEquals("USA", savedPublisher.getCountry());
        assertEquals("40", savedPublisher.getYearsOfEstablishment());
        assertEquals("ea.com", savedPublisher.getWebsite());
        assertEquals("1982", savedPublisher.getFounded());

        verify(publisherRepository, times(1)).save(any(Publisher.class));
    }

    @Test
    void deletePublisher_success() {
        Long id = 1L;
        Publisher publisher = new Publisher();
        publisher.setId(id);

        when(publisherRepository.findById(id)).thenReturn(Optional.of(publisher));

        publisherService.deletePublisher(id);

        verify(publisherRepository, times(1)).delete(publisher);
    }

    @Test
    void deletePublisher_notFound_throwsException() {
        Long id = 99L;
        when(publisherRepository.findById(id)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> publisherService.deletePublisher(id));

        assertEquals("Publisher not found", exception.getMessage());
        verify(publisherRepository, never()).delete(any());
    }

    @Test
    void updatePublisher_fullUpdate_success() {
        Long id = 1L;
        Publisher existingPublisher = new Publisher();
        existingPublisher.setName("Old Name");
        existingPublisher.setCountry("Old Country");

        UpdatePublisherDto dto = new UpdatePublisherDto();
        dto.setName("New Name");
        dto.setCountry("New Country");
        dto.setYearsOfEstablishment("10");
        dto.setWebsite("new.com");
        dto.setFounded("2010");

        when(publisherRepository.findById(id)).thenReturn(Optional.of(existingPublisher));
        when(publisherRepository.save(any(Publisher.class))).thenAnswer(i -> i.getArguments()[0]);

        Publisher updatedPublisher = publisherService.updatePublisher(id, dto);

        assertEquals("New Name", updatedPublisher.getName());
        assertEquals("New Country", updatedPublisher.getCountry());
        assertEquals("10", updatedPublisher.getYearsOfEstablishment());
        assertEquals("new.com", updatedPublisher.getWebsite());
        assertEquals("2010", updatedPublisher.getFounded());

        verify(publisherRepository, times(1)).save(existingPublisher);
    }

    @Test
    void updatePublisher_partialUpdate_success() {
        Long id = 1L;
        Publisher existingPublisher = new Publisher();
        existingPublisher.setName("Ubisoft");
        existingPublisher.setCountry("France");
        existingPublisher.setWebsite("ubisoft.com");

        UpdatePublisherDto dto = new UpdatePublisherDto();
        dto.setWebsite("ubisoft.fr");

        when(publisherRepository.findById(id)).thenReturn(Optional.of(existingPublisher));
        when(publisherRepository.save(any(Publisher.class))).thenAnswer(i -> i.getArguments()[0]);

        Publisher updatedPublisher = publisherService.updatePublisher(id, dto);

        assertEquals("Ubisoft", updatedPublisher.getName());
        assertEquals("France", updatedPublisher.getCountry());
        assertEquals("ubisoft.fr", updatedPublisher.getWebsite());

        verify(publisherRepository, times(1)).save(existingPublisher);
    }

    @Test
    void updatePublisher_notFound_throwsException() {
        Long id = 99L;
        UpdatePublisherDto dto = new UpdatePublisherDto();

        when(publisherRepository.findById(id)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> publisherService.updatePublisher(id, dto));

        assertEquals("Publisher not found", exception.getMessage());
        verify(publisherRepository, never()).save(any());
    }

    @Test
    void getPublisher_success() {
        Long id = 1L;

        Publisher publisher = new Publisher();
        publisher.setId(id);
        publisher.setName("Ubisoft");
        publisher.setCountry("France");
        publisher.setFounded("1986");
        publisher.setWebsite("ubisoft.com");
        publisher.setYearsOfEstablishment("38");

        when(publisherRepository.findById(id)).thenReturn(Optional.of(publisher));

        ShowPublisherDto result = publisherService.getPublisher(id);

        assertNotNull(result);
        assertEquals("Ubisoft", result.getName());
        assertEquals("France", result.getCountry());
        assertEquals("1986", result.getFounded());
        assertEquals("ubisoft.com", result.getWebsite());
        assertEquals("38", result.getYearsOfEstablishment());
    }

    @Test
    void getPublisher_notFound_throwsException() {
        when(publisherRepository.findById(99L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> publisherService.getPublisher(99L));

        assertEquals("Publisher not found", exception.getMessage());
        verify(publisherRepository).findById(99L);
    }

}
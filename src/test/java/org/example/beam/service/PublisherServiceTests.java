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
        CreatePublisherDto dto = new CreatePublisherDto(
        "Electronic Arts",
        "USA",
        "40",
        "ea.com",
        "1982"
        );

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

    }

    @Test
    void updatePublisher_partialUpdate_success() {

    }

    @Test
    void updatePublisher_notFound_throwsException() {

    }

    @Test
    void getPublisher_success() {

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
package com.Ivcho.beam.service;

import com.Ivcho.beam.dto.*;
import com.Ivcho.beam.mapper.PublisherMapper;
import com.Ivcho.beam.model.Publisher;
import com.Ivcho.beam.repository.PublisherRepository;
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

    @Mock
    private PublisherMapper publisherMapper;

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
        Long id = 1L;
        Publisher publisher = new Publisher();
        publisher.setId(id);
        publisher.setName("Old Name");
        publisher.setCountry("Old Country");

        UpdatePublisherDto dto = new UpdatePublisherDto("New Name", "New Country", "50", "new.com", "1990");

        when(publisherRepository.findById(id)).thenReturn(Optional.of(publisher));
        when(publisherRepository.save(any(Publisher.class))).thenAnswer(i -> i.getArguments()[0]);

        Publisher result = publisherService.updatePublisher(id, dto);

        assertEquals("New Name", result.getName());
        assertEquals("New Country", result.getCountry());
        assertEquals("50", result.getYearsOfEstablishment());
        assertEquals("new.com", result.getWebsite());
        assertEquals("1990", result.getFounded());
        verify(publisherRepository).save(publisher);
    }

    @Test
    void updatePublisher_partialUpdate_success() {
        Long id = 1L;
        Publisher publisher = new Publisher();
        publisher.setId(id);
        publisher.setName("Old Name");
        publisher.setCountry("Old Country");

        UpdatePublisherDto dto = new UpdatePublisherDto("New Name", null, null, null, null);

        when(publisherRepository.findById(id)).thenReturn(Optional.of(publisher));
        when(publisherRepository.save(any(Publisher.class))).thenAnswer(i -> i.getArguments()[0]);

        Publisher result = publisherService.updatePublisher(id, dto);

        assertEquals("New Name", result.getName());
        assertEquals("Old Country", result.getCountry());
        verify(publisherRepository).save(publisher);
    }

    @Test
    void updatePublisher_notFound_throwsException() {
        when(publisherRepository.findById(99L)).thenReturn(Optional.empty());

        UpdatePublisherDto dto = new UpdatePublisherDto("Name", null, null, null, null);

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> publisherService.updatePublisher(99L, dto));

        assertEquals("Publisher not found", exception.getMessage());
        verify(publisherRepository, never()).save(any());
    }

    @Test
    void getPublisher_success() {
        Long id = 1L;
        Publisher publisher = new Publisher();
        publisher.setId(id);
        publisher.setName("Electronic Arts");
        publisher.setCountry("USA");
        publisher.setYearsOfEstablishment("40");
        publisher.setWebsite("ea.com");
        publisher.setFounded("1982");

        ShowPublisherDto expectedDto = new ShowPublisherDto("Electronic Arts"
                , "USA"
                , "40"
                , "ea.com"
                , "1982");

        when(publisherRepository.findById(id)).thenReturn(Optional.of(publisher));
        when(publisherMapper.toDto(publisher)).thenReturn(expectedDto);

        ShowPublisherDto result = publisherService.getPublisher(id);

        assertNotNull(result);
        assertEquals("Electronic Arts", result.name());
        assertEquals("USA", result.country());
        assertEquals("40", result.yearsOfEstablishment());
        assertEquals("ea.com", result.website());
        assertEquals("1982", result.founded());
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
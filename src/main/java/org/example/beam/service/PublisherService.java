package org.example.beam.service;

import jakarta.transaction.Transactional;
import org.example.beam.dto.CreatePublisherDto;
import org.example.beam.dto.UpdatePublisherDto;
import org.example.beam.model.Publisher;
import org.example.beam.repository.PublisherRepository;
import org.springframework.stereotype.Service;

@Service
public class PublisherService {
    private final PublisherRepository publisherRepository;

    public PublisherService(PublisherRepository publisherRepository) {
        this.publisherRepository = publisherRepository;
    }

    @Transactional
    public Publisher createPublisher(CreatePublisherDto createPublisherDto) {
        Publisher publisher = new Publisher();
        publisher.setName(createPublisherDto.getName());
        publisher.setCountry(createPublisherDto.getCountry());
        publisher.setYearsOfEstablishment(createPublisherDto.getYearsOfEstablishment());
        publisher.setWebsite(createPublisherDto.getWebsite());
        publisher.setFounded(createPublisherDto.getFounded());
        return publisherRepository.save(publisher);
    }
    @Transactional
    public void deletePublisher(Long id) {
        Publisher publisher = publisherRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Publisher not found"));
        publisherRepository.delete(publisher);
    }

    @Transactional
    public Publisher updatePublisher(Long id, UpdatePublisherDto updatePublisherDto) {
        Publisher publisher = publisherRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Publisher not found"));

        if (updatePublisherDto.getName() != null) {
            publisher.setName(updatePublisherDto.getName());
        }
        if (updatePublisherDto.getCountry() != null) {
            publisher.setCountry(updatePublisherDto.getCountry());
        }
        if (updatePublisherDto.getYearsOfEstablishment() != null) {
            publisher.setYearsOfEstablishment(updatePublisherDto.getYearsOfEstablishment());
        }
        if (updatePublisherDto.getWebsite() != null) {
            publisher.setWebsite(updatePublisherDto.getWebsite());
        }
        if (updatePublisherDto.getFounded() != null) {
            publisher.setFounded(updatePublisherDto.getFounded());
        }

        return publisherRepository.save(publisher);

    }
}
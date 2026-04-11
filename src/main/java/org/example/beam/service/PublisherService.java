package org.example.beam.service;

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

    public Publisher createPublisher(CreatePublisherDto dto) {
        Publisher publisher = new Publisher();
        publisher.setName(dto.getName());
        publisher.setCountry(dto.getCountry());
        publisher.setYearsOfEstablishment(dto.getYearsOfEstablishment());
        publisher.setWebsite(dto.getWebsite());
        publisher.setFounded(dto.getFounded());
        return publisherRepository.save(publisher);
    }

    public void deletePublisher(Long id) {
        Publisher publisher = publisherRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Publisher not found"));
        publisherRepository.delete(publisher);
    }

    public Publisher updatePublisher(UpdatePublisherDto dto) {
        Publisher publisher = publisherRepository.findById(dto.getId())
                .orElseThrow(() -> new RuntimeException("Publisher not found"));

        if (dto.getName() != null) {
            publisher.setName(dto.getName());
        }
        if (dto.getCountry() != null) {
            publisher.setCountry(dto.getCountry());
        }
        if (dto.getYearsOfEstablishment() != null) {
            publisher.setYearsOfEstablishment(dto.getYearsOfEstablishment());
        }
        if (dto.getWebsite() != null) {
            publisher.setWebsite(dto.getWebsite());
        }
        if (dto.getFounded() != null) {
            publisher.setFounded(dto.getFounded());
        }

        return publisherRepository.save(publisher);

    }
}
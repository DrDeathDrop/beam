package com.Ivcho.beam.service;

import jakarta.transaction.Transactional;
import com.Ivcho.beam.dto.CreatePublisherDto;
import com.Ivcho.beam.dto.ShowPublisherDto;
import com.Ivcho.beam.dto.UpdatePublisherDto;
import com.Ivcho.beam.mapper.PublisherMapper;
import com.Ivcho.beam.model.Publisher;
import com.Ivcho.beam.repository.PublisherRepository;
import org.springframework.stereotype.Service;

@Service
public class PublisherService {
    private final PublisherRepository publisherRepository;
    private final PublisherMapper publisherMapper;

    public PublisherService(PublisherRepository publisherRepository, PublisherMapper publisherMapper) {
        this.publisherRepository = publisherRepository;
        this.publisherMapper = publisherMapper;
    }

    @Transactional
    public Publisher createPublisher(CreatePublisherDto createPublisherDto) {
        Publisher publisher = new Publisher();
        publisher.setName(createPublisherDto.name());
        publisher.setCountry(createPublisherDto.country());
        publisher.setYearsOfEstablishment(createPublisherDto.yearsOfEstablishment());
        publisher.setWebsite(createPublisherDto.website());
        publisher.setFounded(createPublisherDto.founded());
        return publisherRepository.save(publisher);
    }
    @Transactional
    public void deletePublisher(Long id) {
        Publisher publisher = publisherRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Publisher not found"));
        publisherRepository.delete(publisher);
    }

    @Transactional
    public ShowPublisherDto getPublisher(Long id) {
        Publisher publisher = publisherRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Publisher not found"));

        return publisherMapper.toDto(publisher);
    }

    @Transactional
    public Publisher updatePublisher(Long id, UpdatePublisherDto updatePublisherDto) {
        Publisher publisher = publisherRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Publisher not found"));

        if (updatePublisherDto.name() != null) {
            publisher.setName(updatePublisherDto.name());
        }
        if (updatePublisherDto.country() != null) {
            publisher.setCountry(updatePublisherDto.country());
        }
        if (updatePublisherDto.yearsOfEstablishment() != null) {
            publisher.setYearsOfEstablishment(updatePublisherDto.yearsOfEstablishment());
        }
        if (updatePublisherDto.website() != null) {
            publisher.setWebsite(updatePublisherDto.website());
        }
        if (updatePublisherDto.founded() != null) {
            publisher.setFounded(updatePublisherDto.founded());
        }

        return publisherRepository.save(publisher);

    }
}
package org.example.beam.controller;

import org.example.beam.dto.*;
import org.example.beam.model.Game;
import org.example.beam.model.Publisher;
import org.example.beam.repository.PublisherRepository;
import org.example.beam.service.PublisherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/publishers")
public class PublisherController {
    @Autowired
    private PublisherRepository publisherRepository;

    @Autowired
    private PublisherService publisherService;

    @PostMapping("/add")
    public String addPublisher(@RequestBody CreatePublisherDto createPublisherDto){
        if (createPublisherDto.getName() == null
                || createPublisherDto.getCountry() == null
                || createPublisherDto.getFounded() == null
                || createPublisherDto.getWebsite() == null
                || createPublisherDto.getYearsOfEstablishment() == null) {
            return "Please provide all the required fields";
        }

        publisherService.createPublisher(createPublisherDto);
        return "Publisher added successfully";
    }

    @PostMapping("/delete/{id}")
    public void deletePublisher(@PathVariable Long id) {
        publisherService.deletePublisher(id);
    }

    @PostMapping("/update/{id}")
    public String updatePublisher(@PathVariable Long id, @RequestBody UpdatePublisherDto updatePublisherDto){
        if (updatePublisherDto.getName() == null
                || updatePublisherDto.getCountry() == null
                || updatePublisherDto.getFounded() == null
                || updatePublisherDto.getWebsite() == null
                || updatePublisherDto.getYearsOfEstablishment() == null) {
            return "Please provide all the required fields";
        }

        publisherService.updatePublisher(id, updatePublisherDto);
        return "Publisher updated successfully";
    }
    @GetMapping("/view/{id}")
    public ShowPublisherDto getPublisher(@PathVariable Long id){
        Publisher publisher = publisherRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Publisher not found"));

        ShowPublisherDto showPublisherDto = new ShowPublisherDto();

        showPublisherDto.setName(publisher.getName());
        showPublisherDto.setCountry(publisher.getCountry());
        showPublisherDto.setFounded(publisher.getFounded());
        showPublisherDto.setWebsite(publisher.getWebsite());
        showPublisherDto.setYearsOfEstablishment(publisher.getYearsOfEstablishment());

        return showPublisherDto;
    }
}

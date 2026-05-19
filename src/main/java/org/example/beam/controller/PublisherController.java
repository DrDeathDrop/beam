package org.example.beam.controller;

import org.example.beam.dto.CreatePublisherDto;
import org.example.beam.dto.ShowPublisherDto;
import org.example.beam.dto.UpdatePublisherDto;
import org.example.beam.service.PublisherService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/publishers")
public class PublisherController {

    private final PublisherService publisherService;

    public PublisherController(PublisherService publisherService) {
        this.publisherService = publisherService;
    }

    @PostMapping("/add")
    public String addPublisher(@RequestBody CreatePublisherDto createPublisherDto) {
        if (createPublisherDto.name() == null
                || createPublisherDto.country() == null
                || createPublisherDto.founded() == null
                || createPublisherDto.website() == null
                || createPublisherDto.yearsOfEstablishment() == null) {
            return "Please provide all the required fields";
        }

        publisherService.createPublisher(createPublisherDto);
        return "Publisher added successfully";
    }

    @GetMapping("/all")
    public List<ShowPublisherDto> getAllPublishers() {
        return publisherService.getAllPublishers();
    }

    @DeleteMapping("/delete/{id}")
    public void deletePublisher(@PathVariable Long id) {

        publisherService.deletePublisher(id);

    }

    @PutMapping("/update/{id}")
    public String updatePublisher(@PathVariable Long id, @RequestBody UpdatePublisherDto updatePublisherDto) {

        publisherService.updatePublisher(id, updatePublisherDto);
        return "Publisher updated successfully";
    }

    @GetMapping("/view/{id}")
    public ShowPublisherDto getPublisher(@PathVariable Long id) {

        return publisherService.getPublisher(id);
    }
}

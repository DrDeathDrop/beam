package org.example.beam.controller;

import org.example.beam.dto.*;
import org.example.beam.service.PublisherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/publishers")
public class PublisherController {

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

    @DeleteMapping("/delete/{id}")
    public void deletePublisher(@PathVariable Long id) {
        publisherService.deletePublisher(id);
    }

    @PutMapping("/update/{id}")
    public String updatePublisher(@PathVariable Long id, @RequestBody UpdatePublisherDto updatePublisherDto){

        publisherService.updatePublisher(id, updatePublisherDto);
        return "Publisher updated successfully";
    }
    @GetMapping("/view/{id}")
    public ShowPublisherDto getPublisher(@PathVariable Long id) {
        return publisherService.getPublisher(id);
    }
}

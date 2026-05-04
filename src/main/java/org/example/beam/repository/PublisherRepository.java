package org.example.beam.repository;

import org.example.beam.model.Publisher;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PublisherRepository extends JpaRepository<Publisher, Long> {

    Optional<Publisher> findById(Long id);
    Optional<Publisher> findByName(String name);
    Optional<Publisher> findByCountry(String country);
    Optional<Publisher> findByYearsOfEstablishment(String yearsOfEstablishment);
    Optional<Publisher> findByWebsite(String website);

}

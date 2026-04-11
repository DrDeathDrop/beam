package org.example.beam.repository;

import org.example.beam.model.Game;
import org.example.beam.model.Publisher;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GameRepository extends JpaRepository<Game, Long> {

    Optional<Game> findById(Long id);
    Optional<Game> findByTitle(String title);
    Optional<Game> findByPublisher(Publisher publisher);
    Optional<Game> findByPrice(Double price);
    Optional <Game> findByGenre(String genre);
}

package com.Ivcho.beam.repository;

import com.Ivcho.beam.model.Game;
import com.Ivcho.beam.model.Publisher;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GameRepository extends JpaRepository<Game, Long> {

    Optional<Game> findById(Long id);
    Optional<Game> findByTitle(String title);
    Optional<Game> findByPublisher(Publisher publisher);
    Optional<Game> findByPrice(Double price);
    Optional <Game> findByGenre(String genre);
}

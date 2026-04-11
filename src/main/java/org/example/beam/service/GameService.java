package org.example.beam.service;

import org.example.beam.dto.*;
import org.example.beam.model.*;
import org.example.beam.repository.*;
import org.springframework.stereotype.Service;

@Service
public class GameService {
    private final GameRepository gameRepository;
    private final PublisherRepository publisherRepository;

    public GameService(GameRepository gameRepository, PublisherRepository publisherRepository) {
        this.gameRepository = gameRepository;
        this.publisherRepository = publisherRepository;
    }

    public void deleteGame(Long id) { 
        Game game = gameRepository.findById(id)
                .orElseThrow(()-> new RuntimeException("Game not found")); 
        gameRepository.delete(game);
    }

    public Game createGame(CreateGameDto dto) {
        Publisher publisher = publisherRepository.findById(dto.getPublisherId())
            .orElseThrow(() -> new RuntimeException("Publisher not found"));

        Game game = new Game();
        game.setTitle(dto.getTitle());
        game.setGenre(dto.getGenre());
        game.setPrice(dto.getPrice());
        game.setPublisher(publisher);
        game.setReleaseDate(dto.getReleaseDate());

        return gameRepository.save(game);
    }

    public Game updateGame(UpdateGameDto dto) {
        Game game = gameRepository.findById(dto.getId())
            .orElseThrow(() -> new RuntimeException("Game not found"));

        if (dto.getTitle() != null) {
            game.setTitle(dto.getTitle());
        }

        if (dto.getGenre() != null) {
            game.setGenre(dto.getGenre());
        }

        if (dto.getPrice() != null) {
            game.setPrice(dto.getPrice());
        }

        if (dto.getReleaseDate() != null) {
            game.setReleaseDate(dto.getReleaseDate());
        }

        if (dto.getDescription() != null) {
            game.setDescription(dto.getDescription());
        }

        if (dto.getPublisherId() != null) {
            Publisher publisher = publisherRepository.findById(dto.getPublisherId())
                .orElseThrow(() -> new RuntimeException("Publisher not found"));
            game.setPublisher(publisher);
        }

        return gameRepository.save(game);
    }
}
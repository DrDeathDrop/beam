package org.example.beam.service;

import jakarta.transaction.Transactional;
import org.example.beam.dto.*;
import org.example.beam.model.*;
import org.example.beam.repository.*;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class GameService {
    private final GameRepository gameRepository;
    private final PublisherRepository publisherRepository;


    public GameService(GameRepository gameRepository, PublisherRepository publisherRepository) {
        this.gameRepository = gameRepository;
        this.publisherRepository = publisherRepository;

    }

    @Transactional
    public ShowGameDto getGame(Long id) {
        Game game = gameRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Game not found"));

        ShowGameDto dto = new ShowGameDto();
        dto.setTitle(game.getTitle());
        dto.setGenre(game.getGenre());
        dto.setPrice(game.getPrice());
        dto.setDescription(game.getDescription());
        dto.setReleaseDate(game.getReleaseDate());
        dto.setPublisherName(game.getPublisher().getName());

        return dto;
    }

    @Transactional
    public void deleteGame(Long id) { 
        Game game = gameRepository.findById(id)
                .orElseThrow(()-> new RuntimeException("Game not found")); 
        gameRepository.delete(game);
    }

    @Transactional
    public Game createGame(CreateGameDto createGameDto) {
        Publisher publisher = publisherRepository.findById(createGameDto.getPublisherId())
            .orElseThrow(() -> new RuntimeException("Publisher not found"));
        Game game = new Game(createGameDto.getTitle(), publisher, createGameDto.getReleaseDate(), createGameDto.getGenre(),  createGameDto.getPrice(),  createGameDto.getDescription());

        return gameRepository.save(game);
    }
    @Transactional
    public Game updateGame(Long id, UpdateGameDto updateGameDto) {
        Game game = gameRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Game not found"));

       if (updateGameDto.getTitle() != null) {
           game.setTitle(updateGameDto.getTitle());
       }

       if (updateGameDto.getGenre() != null) {
           game.setGenre(updateGameDto.getGenre());
       }

       if (updateGameDto.getPrice() != null) {
           game.setPrice(updateGameDto.getPrice());
       }

       if (updateGameDto.getReleaseDate() != null) {
           game.setReleaseDate(updateGameDto.getReleaseDate());
       }

       if (updateGameDto.getDescription() != null) {
           game.setDescription(updateGameDto.getDescription());
       }

       if (updateGameDto.getPublisherId() != null) {
           Publisher publisher = publisherRepository.findById(updateGameDto.getPublisherId())
               .orElseThrow(() -> new RuntimeException("Publisher not found"));
           game.setPublisher(publisher);
       }

        return gameRepository.save(game);
    }
}
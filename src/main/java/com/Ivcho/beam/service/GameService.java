package com.Ivcho.beam.service;

import com.Ivcho.beam.dto.CreateGameDto;
import com.Ivcho.beam.dto.ShowGameDto;
import com.Ivcho.beam.dto.UpdateGameDto;
import com.Ivcho.beam.model.Game;
import com.Ivcho.beam.model.Publisher;
import com.Ivcho.beam.repository.GameRepository;
import com.Ivcho.beam.repository.PublisherRepository;
import jakarta.transaction.Transactional;

import com.Ivcho.beam.mapper.GameMapper;

import org.springframework.stereotype.Service;

@Service
public class GameService {
    private final GameRepository gameRepository;
    private final PublisherRepository publisherRepository;
    private final GameMapper gameMapper;


    public GameService(GameRepository gameRepository, PublisherRepository publisherRepository, GameMapper gameMapper) {
        this.gameRepository = gameRepository;
        this.publisherRepository = publisherRepository;
        this.gameMapper = gameMapper;
    }

    @Transactional
    public ShowGameDto getGame(Long id) {
        Game game = gameRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Game not found"));

        return gameMapper.toDto(game);
    }

    @Transactional
    public void deleteGame(Long id) { 
        Game game = gameRepository.findById(id)
                .orElseThrow(()-> new RuntimeException("Game not found")); 
        gameRepository.delete(game);
    }

    @Transactional
    public Game createGame(CreateGameDto createGameDto) {
        Publisher publisher = publisherRepository.findById(createGameDto.publisherId())
                .orElseThrow(() -> new RuntimeException("Publisher not found"));
        Game game = new Game(createGameDto.title(), publisher, createGameDto.releaseDate(),
                createGameDto.genre(), createGameDto.price(), createGameDto.description());

        return gameRepository.save(game);
    }

    @Transactional
    public Game updateGame(Long id, UpdateGameDto updateGameDto) {
        Game game = gameRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Game not found"));

       if (updateGameDto.title() != null) {
           game.setTitle(updateGameDto.title());
       }

       if (updateGameDto.genre() != null) {
           game.setGenre(updateGameDto.genre());
       }

       if (updateGameDto.price() != null) {
           game.setPrice(updateGameDto.price());
       }

       if (updateGameDto.releaseDate() != null) {
           game.setReleaseDate(updateGameDto.releaseDate());
       }

       if (updateGameDto.description() != null) {
           game.setDescription(updateGameDto.description());
       }

       if (updateGameDto.publisherId() != null) {
           Publisher publisher = publisherRepository.findById(updateGameDto.publisherId())
               .orElseThrow(() -> new RuntimeException("Publisher not found"));
           game.setPublisher(publisher);
       }

        return gameRepository.save(game);
    }
}
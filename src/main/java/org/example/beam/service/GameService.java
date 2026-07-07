package org.example.beam.service;

import org.example.beam.dto.CreateGameDto;
import org.example.beam.dto.ShowGameDto;
import org.example.beam.dto.UpdateGameDto;
import org.example.beam.model.Game;
import org.example.beam.model.Publisher;
import org.example.beam.repository.GameRepository;
import org.example.beam.repository.PublisherRepository;
import org.example.beam.repository.PurchaseRepository;
import jakarta.transaction.Transactional;

import org.example.beam.mapper.GameMapper;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GameService {
    private final GameRepository gameRepository;
    private final PublisherRepository publisherRepository;
    private final PurchaseRepository purchaseRepository;
    private final GameMapper gameMapper;


    public GameService(GameRepository gameRepository, PublisherRepository publisherRepository, PurchaseRepository purchaseRepository, GameMapper gameMapper) {
        this.gameRepository = gameRepository;
        this.publisherRepository = publisherRepository;
        this.purchaseRepository = purchaseRepository;
        this.gameMapper = gameMapper;
    }

    @Transactional
    public ShowGameDto getGame(Long id) {
        Game game = gameRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Game not found"));

        return gameMapper.toDto(game);
    }

    public List<ShowGameDto> getAllGames() {
        return gameRepository.findAll()
                .stream()
                .map(gameMapper::toDto)
                .toList();
    }

    @Transactional
    public void deleteGame(Long id) {
        Game game = gameRepository.findById(id)
                .orElseThrow(()-> new RuntimeException("Game not found"));
        // Remove purchases that reference this game first, otherwise the
        // purchase.game_id foreign key blocks the delete.
        purchaseRepository.deleteAllByGame_Id(id);
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
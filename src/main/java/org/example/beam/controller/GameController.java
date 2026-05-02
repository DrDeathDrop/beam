package org.example.beam.controller;

import org.example.beam.dto.*;

import org.example.beam.mapper.GameMapper;
import org.example.beam.model.Game;
import org.example.beam.repository.GameRepository;
import org.example.beam.service.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/games")
public class GameController {
    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private GameService gameService;

    @Autowired
    private GameMapper gameMapper;

    @PostMapping("/add")
    public String addGame(@RequestBody CreateGameDto createGameDto){
        if (createGameDto == null
                ||createGameDto.getTitle() == null
                || createGameDto.getPrice() == null
                || createGameDto.getGenre() == null
                || createGameDto.getDescription() == null
                || createGameDto.getReleaseDate() == null
                || createGameDto.getPublisherId() == null) {
            return "Please provide all the required fields";
        }
        gameService.createGame(createGameDto);
        return "Game added successfully";
    }

    @DeleteMapping("/delete/{id}")
    public void deleteGame(@PathVariable Long id) {
        gameService.deleteGame(id);
    }

    @PutMapping("/update/{id}")
    public String updateGame(@PathVariable Long id, @RequestBody UpdateGameDto updateGameDto){

        gameService.updateGame(id, updateGameDto);
        return "Game updated successfully";
    }
    
    @GetMapping("/view/{id}")
    public ShowGameDto getGame(@PathVariable Long id) {
        Game game = gameRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Game not found"));

        return gameMapper.toDto(game);
    }
}

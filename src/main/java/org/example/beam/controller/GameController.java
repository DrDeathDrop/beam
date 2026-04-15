package org.example.beam.controller;

import org.example.beam.dto.*;

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

    @PostMapping("/add")
    public String addGame(@RequestBody CreateGameDto createGameDto){
        if (createGameDto.getTitle() == null
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

    @PostMapping("/delete/{id}")
    public void deleteGame(@PathVariable Long id) {
        gameService.deleteGame(id);
    }

    @PostMapping("/update/{id}")
    public String updateGame(@PathVariable Long id, @RequestBody UpdateGameDto updateGameDto){

        gameService.updateGame(id, updateGameDto);
        return "Game updated successfully";
    }
    @GetMapping("/view/{id}")
    public ShowGameDto getGame(@PathVariable Long id) {
        Game game = gameRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Game not found"));

        ShowGameDto showGameDto = new ShowGameDto();

        showGameDto.setTitle(game.getTitle());
        showGameDto.setGenre(game.getGenre());
        showGameDto.setPrice(game.getPrice());
        showGameDto.setDescription(game.getDescription());
        showGameDto.setReleaseDate(game.getReleaseDate());
        showGameDto.setPublisherName(game.getPublisher().getName());

        return showGameDto;
    }
}

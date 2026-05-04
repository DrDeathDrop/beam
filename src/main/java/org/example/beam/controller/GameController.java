package org.example.beam.controller;

import org.example.beam.dto.*;
import org.example.beam.service.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/games")
public class GameController {

    @Autowired
    private GameService gameService;

    @PostMapping("/add")
    public String addGame(@RequestBody CreateGameDto createGameDto){
        if (createGameDto == null
                ||createGameDto.title() == null
                || createGameDto.price() == null
                || createGameDto.genre() == null
                || createGameDto.description() == null
                || createGameDto.releaseDate() == null
                || createGameDto.publisherId() == null) {
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
        return gameService.getGame(id);
    }
}

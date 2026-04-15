package org.example.beam.controller;

import org.example.beam.dto.*;
import org.example.beam.model.Game;
import org.example.beam.model.Publisher;
import org.example.beam.repository.GameRepository;
import org.example.beam.service.GameService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GameControllerTests {

    @InjectMocks
    private GameController gameController;

    @Mock
    private GameRepository gameRepository;

    @Mock
    private GameService gameService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void addGame_success() {
        CreateGameDto dto = new CreateGameDto();
        dto.setTitle("Test Game");
        dto.setPrice(BigDecimal.valueOf(59.99));
        dto.setGenre("Action");
        dto.setDescription("Cool game");
        dto.setReleaseDate("2024");
        dto.setPublisherId(1L);

        String result = gameController.addGame(dto);

        assertEquals("Game added successfully", result);
        verify(gameService, times(1)).createGame(dto);
    }

    @Test
    void addGame_missingField_returnsError() {
        CreateGameDto dto = new CreateGameDto();
        dto.setTitle(null);
        String result = gameController.addGame(dto);

        assertEquals("Please provide all the required fields", result);
        verify(gameService, never()).createGame(any());
    }

    @Test
    void deleteGame_callsService() {
        Long id = 5L;
        gameController.deleteGame(id);
        verify(gameService).deleteGame(id);
    }

    @Test
    void updateGame_success() {
        Long id = 4L;
        UpdateGameDto dto = new UpdateGameDto();
        dto.setTitle("Updated Title");
        dto.setPrice(BigDecimal.valueOf(49.99));
        dto.setGenre("RPG");
        dto.setDescription("New Description");
        dto.setReleaseDate("2025");
        dto.setPublisherId(2L);

        String result = gameController.updateGame(id, dto);

        assertEquals("Game updated successfully", result);
        verify(gameService).updateGame(id, dto);
    }


    @Test
    void getGame_success() {
        Publisher publisher = new Publisher();
        publisher.setName("Indie Studio");

        Game game = new Game();
        game.setTitle("TGame");
        game.setGenre("Adventure");
        game.setPrice(BigDecimal.valueOf(19.99));
        game.setDescription("Nice adventure");
        game.setReleaseDate("2019");
        game.setPublisher(publisher);

        when(gameRepository.findById(11L)).thenReturn(Optional.of(game));

        ShowGameDto resultDto = gameController.getGame(11L);

        assertNotNull(resultDto);
        assertEquals("TGame", resultDto.getTitle());
        assertEquals("Indie Studio", resultDto.getPublisherName());
    }

    @Test
    void getGame_notFound_throwsException() {
        Long id = 88L;
        when(gameRepository.findById(id)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            gameController.getGame(id);
        });

        assertEquals("Game not found", exception.getMessage());
    }
}
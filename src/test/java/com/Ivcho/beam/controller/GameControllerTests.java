package com.Ivcho.beam.controller;

import com.Ivcho.beam.dto.CreateGameDto;
import com.Ivcho.beam.dto.ShowGameDto;
import com.Ivcho.beam.dto.UpdateGameDto;
import org.example.beam.dto.*;
import com.Ivcho.beam.repository.GameRepository;
import com.Ivcho.beam.service.GameService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.math.BigDecimal;

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
        CreateGameDto dto = new CreateGameDto("Test Game", "Action", BigDecimal.valueOf(59.99), 1L, "Cool game", "2024");

        String result = gameController.addGame(dto);

        assertEquals("Game added successfully", result);
        verify(gameService, times(1)).createGame(dto);
    }

    @Test
    void addGame_missingField_returnsError() {
        CreateGameDto dto = new CreateGameDto(null, null, null, null, null, null);

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
        Long id = 1L;
        UpdateGameDto dto = new UpdateGameDto("Updated Title", "RPG", BigDecimal.valueOf(39.99), "2025", "New desc", null);

        String result = gameController.updateGame(id, dto);

        assertEquals("Game updated successfully", result);
        verify(gameService).updateGame(id, dto);
    }


    @Test
    void getGame_success() {
        ShowGameDto dto = new ShowGameDto(1L, "TGame", null, null, "Indie Studio", null, null);

        when(gameService.getGame(11L)).thenReturn(dto);

        ShowGameDto result = gameController.getGame(11L);

        assertNotNull(result);
        assertEquals("TGame", result.title());
        assertEquals("Indie Studio", result.publisherName());
    }

    @Test
    void getGame_notFound_throwsException() {
        when(gameService.getGame(88L)).thenThrow(new RuntimeException("Game not found"));

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> gameController.getGame(88L));

        assertEquals("Game not found", exception.getMessage());
    }
}
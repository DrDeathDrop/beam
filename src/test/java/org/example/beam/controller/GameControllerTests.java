package org.example.beam.controller;

import org.example.beam.dto.CreateGameDto;
import org.example.beam.dto.ShowGameDto;
import org.example.beam.dto.UpdateGameDto;
import org.example.beam.service.GameService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GameControllerTests {

    @InjectMocks
    private GameController gameController;

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
    void addGame_missingTitle_returnsError() {
        CreateGameDto dto = new CreateGameDto(null, "Action", BigDecimal.valueOf(59.99), 1L, "Cool game", "2024");

        String result = gameController.addGame(dto);

        assertEquals("Please provide all the required fields", result);
        verify(gameService, never()).createGame(any());
    }

    @Test
    void addGame_missingGenre_returnsError() {
        CreateGameDto dto = new CreateGameDto("Test Game", null, BigDecimal.valueOf(59.99), 1L, "Cool game", "2024");

        String result = gameController.addGame(dto);

        assertEquals("Please provide all the required fields", result);
        verify(gameService, never()).createGame(any());
    }

    @Test
    void addGame_missingPrice_returnsError() {
        CreateGameDto dto = new CreateGameDto("Test Game", "Action", null, 1L, "Cool game", "2024");

        String result = gameController.addGame(dto);

        assertEquals("Please provide all the required fields", result);
        verify(gameService, never()).createGame(any());
    }

    @Test
    void addGame_allFieldsNull_returnsError() {
        CreateGameDto dto = new CreateGameDto(null, null, null, null, null, null);

        String result = gameController.addGame(dto);

        assertEquals("Please provide all the required fields", result);
        verify(gameService, never()).createGame(any());
    }

    @Test
    void deleteGame_callsService() {
        gameController.deleteGame(5L);

        verify(gameService, times(1)).deleteGame(5L);
    }

    @Test
    void deleteGame_notFound_throwsException() {
        doThrow(new RuntimeException("Game not found"))
                .when(gameService).deleteGame(99L);

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> gameController.deleteGame(99L));

        assertEquals("Game not found", ex.getMessage());
    }

    @Test
    void updateGame_success() {
        UpdateGameDto dto = new UpdateGameDto("Updated Title", "RPG", BigDecimal.valueOf(39.99), "2025", "New desc", null);

        String result = gameController.updateGame(1L, dto);

        assertEquals("Game updated successfully", result);
        verify(gameService, times(1)).updateGame(1L, dto);
    }

    @Test
    void updateGame_partialFields_success() {
        UpdateGameDto dto = new UpdateGameDto("Only Title Changed", null, null, null, null, null);

        String result = gameController.updateGame(1L, dto);

        assertEquals("Game updated successfully", result);
        verify(gameService, times(1)).updateGame(1L, dto);
    }

    @Test
    void updateGame_notFound_throwsException() {
        UpdateGameDto dto = new UpdateGameDto("Title", null, null, null, null, null);
        doThrow(new RuntimeException("Game not found"))
                .when(gameService).updateGame(99L, dto);

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> gameController.updateGame(99L, dto));

        assertEquals("Game not found", ex.getMessage());
    }

    @Test
    void getGame_success() {
        ShowGameDto dto = new ShowGameDto(1L, "Test Game", "Action", BigDecimal.valueOf(59.99), 12L, "Cool game", "2024", "PublisherName");
        when(gameService.getGame(1L)).thenReturn(dto);

        ShowGameDto result = gameController.getGame(1L);

        assertNotNull(result);
        assertEquals("Test Game", result.title());
        assertEquals(12L, result.publisherName());
    }

    @Test
    void getGame_notFound_throwsException() {
        when(gameService.getGame(88L)).thenThrow(new RuntimeException("Game not found"));

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> gameController.getGame(88L));

        assertEquals("Game not found", ex.getMessage());
    }


    @Test
    void getAllGames_returnsList() {
        List<ShowGameDto> games = List.of(
                new ShowGameDto(1L, "Game One", "Action", BigDecimal.valueOf(29.99), 1L, "Desc", "2023", "Publisher A"),
                new ShowGameDto(2L, "Game Two", "RPG",    BigDecimal.valueOf(49.99), 2L, "Desc", "2024", "Publisher B")
        );
        when(gameService.getAllGames()).thenReturn(games);

        List<ShowGameDto> result = gameController.getAllGames();

        assertEquals(2, result.size());
        assertEquals("Game One", result.get(0).title());
        assertEquals("Game Two", result.get(1).title());
        verify(gameService, times(1)).getAllGames();
    }

    @Test
    void getAllGames_emptyList() {
        when(gameService.getAllGames()).thenReturn(List.of());

        List<ShowGameDto> result = gameController.getAllGames();

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
}
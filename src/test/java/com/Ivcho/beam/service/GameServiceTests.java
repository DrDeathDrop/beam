package com.Ivcho.beam.service;

import com.Ivcho.beam.dto.CreateGameDto;
import com.Ivcho.beam.dto.ShowGameDto;
import com.Ivcho.beam.dto.UpdateGameDto;
import com.Ivcho.beam.model.Game;
import com.Ivcho.beam.model.Publisher;
import com.Ivcho.beam.repository.GameRepository;
import com.Ivcho.beam.repository.PublisherRepository;
import org.example.beam.dto.*;
import com.Ivcho.beam.mapper.GameMapper;
import org.example.beam.model.*;
import org.example.beam.repository.*;
import org.junit.jupiter.api.*;
import org.mockito.*;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GameServiceTests {

    @InjectMocks
    private GameService gameService;

    @Mock
    private GameRepository gameRepository;

    @Mock
    private PublisherRepository publisherRepository;

    @Mock
    private GameMapper gameMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createGame_success() {
        CreateGameDto dto = new CreateGameDto(
                "Test Game",
                "Action",
                BigDecimal.valueOf(59.99),
                1L,
                "Cool game",
                "2024"
        );

        Publisher publisher = new Publisher();
        publisher.setId(1L);
        publisher.setName("Epic Games");

        when(publisherRepository.findById(1L)).thenReturn(Optional.of(publisher));
        when(gameRepository.save(any(Game.class))).thenAnswer(i -> i.getArguments()[0]);

        Game savedGame = gameService.createGame(dto);

        assertNotNull(savedGame);
        assertEquals("Test Game", savedGame.getTitle());
        assertEquals("Epic Games", savedGame.getPublisher().getName());
        verify(gameRepository).save(any(Game.class));
    }

    @Test
    void createGame_publisherNotFound_throwsException() {
        CreateGameDto dto = new CreateGameDto(null, null, null, 99L, null, null);

        when(publisherRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> gameService.createGame(dto));
        verify(gameRepository, never()).save(any());
    }

    @Test
    void deleteGame_success() {
        Long gameId = 1L;
        Game game = new Game();
        game.setId(gameId);

        when(gameRepository.findById(gameId)).thenReturn(Optional.of(game));

        gameService.deleteGame(gameId);

        verify(gameRepository).delete(game);
    }

    @Test
    void deleteGame_notFound_throwsException() {
        when(gameRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> gameService.deleteGame(1L));
        verify(gameRepository, never()).delete(any());
    }

    @Test
    void updateGame_fullUpdate_success() {
        Long gameId = 1L;
        Game existingGame = new Game();
        existingGame.setTitle("Old Title");

        Publisher newPublisher = new Publisher();
        newPublisher.setName("New Publisher");

        UpdateGameDto updateDto = new UpdateGameDto("New Title", null, null, null, null, 2L);

        when(gameRepository.findById(gameId)).thenReturn(Optional.of(existingGame));
        when(publisherRepository.findById(2L)).thenReturn(Optional.of(newPublisher));
        when(gameRepository.save(any(Game.class))).thenAnswer(i -> i.getArguments()[0]);

        Game updatedGame = gameService.updateGame(gameId, updateDto);

        assertEquals("New Title", updatedGame.getTitle());
        assertEquals("New Publisher", updatedGame.getPublisher().getName());
        verify(gameRepository).save(existingGame);
    }

    @Test
    void updateGame_partialUpdate_onlyTitle() {
        Long gameId = 1L;
        Game existingGame = new Game();
        existingGame.setTitle("Old Title");
        existingGame.setGenre("Action");

        UpdateGameDto updateDto = new UpdateGameDto("Only Title Updated", null, null, null, null, null);

        when(gameRepository.findById(gameId)).thenReturn(Optional.of(existingGame));
        when(gameRepository.save(any(Game.class))).thenAnswer(i -> i.getArguments()[0]);

        Game result = gameService.updateGame(gameId, updateDto);

        assertEquals("Only Title Updated", result.getTitle());
        assertEquals("Action", result.getGenre());
        verify(publisherRepository, never()).findById(anyLong());
    }

    @Test
    void updateGame_notFound_throwsException() {
        when(gameRepository.findById(99L)).thenReturn(Optional.empty());

        UpdateGameDto dto = new UpdateGameDto("Title", null, null, null, null, null);

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> gameService.updateGame(99L, dto));

        assertEquals("Game not found", exception.getMessage());
        verify(gameRepository, never()).save(any());
    }

    @Test
    void getGame_success() {
        Long gameId = 1L;

        Publisher publisher = new Publisher();
        publisher.setName("Test Publisher");

        Game game = new Game();
        game.setId(gameId);
        game.setTitle("Test Game");
        game.setGenre("Action");
        game.setPrice(BigDecimal.valueOf(49.99));
        game.setDescription("A test game");
        game.setReleaseDate("2024");
        game.setPublisher(publisher);

        ShowGameDto expectedDto = new ShowGameDto(gameId, "Test Game", "Action",
                BigDecimal.valueOf(49.99), "Test Publisher", "A test game", "2024");

        when(gameRepository.findById(gameId)).thenReturn(Optional.of(game));
        when(gameMapper.toDto(game)).thenReturn(expectedDto);

        ShowGameDto result = gameService.getGame(gameId);

        assertNotNull(result);
        assertEquals("Test Game", result.title());
        assertEquals("Action", result.genre());
        assertEquals(BigDecimal.valueOf(49.99), result.price());
        assertEquals("A test game", result.description());
        assertEquals("2024", result.releaseDate());
        assertEquals("Test Publisher", result.publisherName());
    }

    @Test
    void getGame_notFound_throwsException() {
        when(gameRepository.findById(99L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> gameService.getGame(99L));

        assertEquals("Game not found", exception.getMessage());
        verify(gameRepository).findById(99L);
    }
}